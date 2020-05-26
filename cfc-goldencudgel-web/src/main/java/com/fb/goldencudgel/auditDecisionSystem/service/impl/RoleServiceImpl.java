package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.comwave.core.data.domain.QueryPage;
import com.comwave.core.jpa.BaseJpaDAO;
import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysFunction;
import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysRole;
import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysRoleFunc;
import com.fb.goldencudgel.auditDecisionSystem.repository.RoleFuncRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.SysFunctionRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.SysRoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RoleServiceImpl extends BaseJpaDAO {

    @Autowired
    private SysFunctionRepository functionRepository;
    @Autowired
    private SysRoleRepository roleRepository;
    @Autowired
    private RoleFuncRepository roleFuncRepository;

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public QueryPage<Object[]> findByNameRoleType(String roleName, QueryPage<Object[]> queryPage) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT  ROLE_ID,ROLE_NAME,ROLE_ENABLE,ORDER_NUM FROM SYS_ROLE  WHERE 1=1 AND ROLE_TYPE='INNER' ");
        if(StringUtils.isNotBlank(roleName)){
            sb.append(" AND ROLE_NAME like:roleName");
            params.put("roleName","%"+roleName+"%");
        }
        return findBySQL(sb, queryPage, params);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public QueryPage<Object[]> findByRoleId(String roleId) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT  ROLE_ID FROM SYS_ROLE  WHERE 1=1");
        if(StringUtils.isNotBlank(roleId)){
            sb.append(" AND ROLE_ID =:roleId");
            params.put("roleId",roleId);
        }
        return findBySQL(sb, params);
    }


    public List<SysFunction> viewFunctionByRole() {
        return functionRepository.viewFunctionByRole();
    }

    public Map<String, Object> saveAddRole(String name,String id,String status,String orderNum){
        Map<String, Object> map = new HashMap<>();
        if(null == id){
            map.put("flag",false);
            map.put("resultMsg","角色代號不能為空,請重新輸入");
            return map;
        }
        //检查是否角色已存在
        QueryPage<Object[]> role = findByRoleId(id.trim());
        if(null != role && role.getContent().size()>0){
            map.put("flag",false);
            map.put("resultMsg","角色已存在,請重新輸入");
            return map;
        }else{
            SysRole newRole = new SysRole();
            newRole.setId(id);
            newRole.setName(name);
            newRole.setOrderNum(orderNum);
            if("1".equals(status)){
                newRole.setStatus(1);
            }else{
                newRole.setStatus(0);
            }
            newRole.setType("INNER");
            roleRepository.save(newRole);
            map.put("flag",true);
            map.put("resultMsg","儲存成功");
            return map;
        }
    }
    /**修改角色信息*/
    public Map<String, Object> saveEditRole(String name,String id,String status,String orderNum){
        Map<String, Object> map = new HashMap<>();
        //查找角色
        SysRole role = roleRepository.findRoleById(id);
        if(null == role){
            map.put("flag",false);
            map.put("resultMsg","角色已被删除");
        }else{
            //修改角色表
            role.setName(name);
            role.setOrderNum(orderNum);
            Integer insertStatus = 1;
            if(!"1".equals(status)){
                insertStatus = 0;
            }
            role.setStatus(insertStatus);
            roleRepository.save(role);
            //修改角色权限表
            List<SysRoleFunc> rfList = roleFuncRepository.findByRoleID(id);
            for(SysRoleFunc rf :rfList){
                rf.setStatus(status);
                roleFuncRepository.save(rf);
            }
            //修改权限表
            for(SysFunction function :role.getFunctions()){
                function.setEnabled(insertStatus);
                functionRepository.save(function);
            }
            map.put("flag",true);
            map.put("resultMsg","修改成功");
        }

        return map;
    }

    public void deleteRole(String id){
        roleFuncRepository.deleteByRoleId(id);
        roleRepository.deleteById(id);
    }



    public QueryPage<Object[]> findAllRoles(String roleId,String authType, QueryPage<Object[]> queryPage){
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT SR.ROLE_NAME,T.ROLE_ID,T.AUTH_TYPE,T.IS_ENABLED FROM SYS_ROLE SR " +
                " INNER JOIN ( " +
                " SELECT ROLE_ID,AUTH_TYPE,IS_ENABLED FROM SYS_ROLE_FUNC  GROUP BY ROLE_ID,AUTH_TYPE,IS_ENABLED" +
                ") T ON SR.ROLE_ID = T.ROLE_ID WHERE 1=1 ");
        if(StringUtils.isNotBlank(roleId)){
            sb.append(" AND T.ROLE_ID =:roleId");
            params.put("roleId",roleId);
        }
        if(StringUtils.isNotBlank(authType)){
            sb.append(" AND T.AUTH_TYPE =:authType");
            params.put("authType",authType);
        }
        return findBySQL(sb, queryPage, params);
    }




    /**根据权限类型查询权限*/
    public List<SysFunction> findFuntionsByType(String type){

        List<SysFunction> funs =  functionRepository.viewFunctionByRoleType(type);
        if(funs == null || funs.size() == 0){
            funs = new ArrayList<>();
        }
        return funs;
    }
    /**新增角色权限*/
    public Map<String, Object> saveRole(String roleId, String enabled, String funcStr ,String authType){
        Map<String, Object> map = new HashMap<>();
        //查询是否已存在 角色-权限纪录
        QueryPage<Object[]> roleFunc =  this.isExsitRoleFunc(roleId,authType);
        if(roleFunc.getContent().size()>0){
            map.put("flag",false);
            map.put("message","儲存失敗，角色已經存在！");
            return map;
        }
        Integer status = 1;
        if(!"1".equals(enabled)){
            status = 0;
        }
        try{
            //儲存角色权限关系表
            String[] functions;
            if(null != funcStr && !"".equals(funcStr)){
                functions = funcStr.split(",");
                for (String funCode:functions){
                    SysFunction sysFunction = functionRepository.findByFuncode(funCode);
                    sysFunction.setEnabled(status);
                    functionRepository.save(sysFunction);
                    SysRoleFunc rf = new SysRoleFunc();
                    rf.setFuncCode(funCode);
                    rf.setRoleId(roleId);
                    rf.setStatus(enabled);
                    rf.setAuthType(authType);
                    roleFuncRepository.save(rf);
                }
            }
            map.put("flag",true);
            map.put("message","儲存成功！");
        }catch (Exception e){
            map.put("flag",false);
            map.put("message","儲存失敗，請查看網路！");
        }
       return map;
    }

    /***根据角色查询角色权限表，如果存在纪录则不允许新增角色*/
    public QueryPage<Object[]> isExsitRoleFunc(String roleId,String authType){
        Map<String,Object> params = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        sb.append("select DISTINCT ROLE_ID from SYS_ROLE_FUNC  where 1=1  ");
        if(StringUtils.isNotBlank(roleId)){
            sb.append(" AND ROLE_ID =:roleId ");
            params.put("roleId",roleId);
        }
        if(StringUtils.isNotBlank(authType)){
            sb.append(" AND AUTH_TYPE =:authType ");
            params.put("authType",authType);
        }
        return findBySQL(sb,params);
    }



    public  QueryPage<Object[]> findRoleFuncs(String roleId,String type , QueryPage<Object[]> queryPage){
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT SR.ROLE_ID,SR.ROLE_NAME,T.IS_ENABLED,T.AUTH_TYPE from SYS_ROLE SR inner join " +
                "(SELECT ROLE_ID,AUTH_TYPE,IS_ENABLED from  SYS_ROLE_FUNC group by ROLE_ID,AUTH_TYPE,IS_ENABLED) T ON SR.ROLE_ID = T.ROLE_ID where 1=1" );
        if (StringUtils.isNotBlank(roleId)){
            sb.append(" and SR.ROLE_ID =:roleId ");
            params.put("roleId",roleId);
        }
        if (StringUtils.isNotBlank(type)){
            sb.append(" and T.AUTH_TYPE =:type ");
            params.put("type",type);
        }
        sb.append(" order by SR.ORDER_NUM ");
        return findBySQL(sb, queryPage, params);
    }

    /**新增角色权限*/
    public Map<String, Object> saveUpdateRF(String roleId, String enabled, String funcStr ,String authType){
        Map<String, Object> map = new HashMap<>();
        //删除旧数据
//        List<SysRoleFunc> roleFunc = roleFuncRepository.findByIdAndAuth(roleId,authType);
//        for(SysRoleFunc s:roleFunc){
//            roleFuncRepository.delete(s);
//        }
        roleFuncRepository.deleteByIdAndAuth(roleId,authType);
        //儲存新数据
        try{
            List<SysRoleFunc> funcList = new ArrayList<>();
            String[] functions = null;
            if(null != funcStr && !"".equals(funcStr)){
                functions = funcStr.split(",");
                for (String funCode:functions){
                    SysRoleFunc rf = new SysRoleFunc();
                    rf.setFuncCode(funCode);
                    rf.setRoleId(roleId);
                    rf.setStatus(enabled);
                    rf.setAuthType(authType);
                    roleFuncRepository.save(rf);
                }
            }
            map.put("flag",true);
            map.put("message","修改成功！");
        }catch (Exception e){
            map.put("flag",false);
            map.put("message","修改失敗，請查看網路！");
        }
        return map;
    }


   /***根据角色查询角色权限表，如果存在纪录则不允许删除角色*/
    public QueryPage<Object[]> isExsitRoleUse(String roleId){
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        sb.append("select DISTINCT ROLE_ID from FB_USER where 1=1  ");
        if(StringUtils.isNotBlank(roleId)){
            sb.append(" AND ROLE_ID =:roleId ");
            params.put("roleId",roleId);
        }
        return findBySQL(sb,params);
    }
    /****/
    public String selectChildBox(String funcode){
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT GROUP_CONCAT(CHILD.FUNC_CODE) AS CHILD_FUNC_CODE  FROM SYS_FUNCTION CHILD INNER JOIN SYS_FUNCTION PARENT " +
                " ON CHILD.PARENT_CODE = PARENT.FUNC_CODE AND CHILD.IS_ENABLED='1' AND PARENT.IS_ENABLED='1' WHERE 1=1 ");
        if(StringUtils.isNotBlank(funcode)){
            sb.append("AND PARENT.FUNC_CODE =:funcode ");
            params.put("funcode",funcode);
        }
        List<Object> result =  findBySQL(sb,params).getContent();
        String childCode = "";
        if(result.size()>0){
            childCode = result.get(0).toString();
        }
        return childCode;
//        SysFunction func = functionRepository.findByFuncode(funcode);
//        StringBuffer sb = new StringBuffer();
//        if(func != null && "1".equals(func.getMenu())){
//            for(SysFunction sf : func.getChildren()){
//                if("1".equals(sf.getMenu())){
//                    for(SysFunction f:sf.getChildren()){
//                        sb.append(f.getCode());
//                        sb.append(",");
//                    }
//                }else{
//                    sb.append(sf.getCode());
//                    sb.append(",");
//                }
//            }
//        }
//        String funList  = sb.toString();
//        if(funList.length()>0){
//            funList = funList.substring(0,funList.length()-1);
//        }
//        return funList;
    }

}
