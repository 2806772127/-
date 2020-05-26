package com.fb.goldencudgel.auditDecisionSystem.controller.role;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysFunction;
import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysRoleFunc;
import com.fb.goldencudgel.auditDecisionSystem.repository.RoleFuncRepository;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.RoleServiceImpl;

/**
 * 权限控制类
 * @author David
 * 2018/12/28
 * */
@Controller
@RequestMapping("/auths")
public class FunctionController {
    private final Logger logger = LoggerFactory.getLogger(FunctionController.class);
    private static final Integer PAGE_SIZE = 20;

    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private RoleFuncRepository roleFuncRepository;

    /**权限查询页面**/
    @RequestMapping("/viewAuths")
    public String viewAllRoles(Model model,@RequestParam(required = false, defaultValue = "1") Integer curPage){

        QueryPage<Object[]> roleList = roleService.findByNameRoleType(null,new QueryPage<>(curPage,PAGE_SIZE));
        model.addAttribute("roleList",roleList);
        return "role/viewAllFunctions";
    }

    /**查询权限**/
    @RequestMapping("/viewFuncList")
    public String viewFuncList(Model model,String roleId,String type,@RequestParam(required = false, defaultValue = "1") Integer curPage){
        QueryPage<Object[]> page = roleService.findRoleFuncs(roleId,type,new QueryPage<>(curPage,PAGE_SIZE) );
        model.addAttribute("page",page);
        model.addAttribute("content",page.getContent());
        return "role/viewFuncList";
    }

    /**权限新增页面**/
    @RequestMapping("/goAddFunctions")
    public String goAddFunctions(Model model,String webVal){
        QueryPage<Object[]> roleList = roleService.findByNameRoleType(null,new QueryPage<>(1,99));
        List<SysFunction> funcList = roleService.findFuntionsByType("1");
        model.addAttribute("authType","1");
        model.addAttribute("funcList",funcList);
        model.addAttribute("roleList",roleList.getContent());
        model.addAttribute("webVal",webVal);
        return "role/viewAddFuntions";
    }

    /**获取所有权限结果集**/
    @RequestMapping("/getFuntions")
    public String getFuntions(Model model,String authType){
        List<SysFunction> topFunctions = roleService.findFuntionsByType(authType);
        model.addAttribute("topFunctions",topFunctions);
        model.addAttribute("authType",authType);
        return "role/addRoleFunctions";
    }

    /**儲存角色权限*/
    @RequestMapping("/saveAddFunctions")
    @ResponseBody
    public Map<String, Object> saveAddFunctions(Model model, String roleId,  String enabled, String funcStr ,String authType){
        Map<String, Object> map =  roleService.saveRole(roleId, enabled, funcStr,authType);
        return map;
    }

    /**删除角色权限*/
    @RequestMapping("/deleteRoleFunc")
    @ResponseBody
    public Map<String, Object> deleteRoleFunc(Model model, String roleId,String authType){
        Map<String, Object> map = new HashMap<>();
         try{
             roleFuncRepository.deleteByIdAndAuth(roleId,authType);
             map.put("flag",true);
             map.put("message","删除成功！");
         }catch (Exception e){
             map.put("flag",false);
             map.put("message","刪除失敗，請查看網路是否正常！");
         }
        return map;
    }

    /**权限修改页面**/
    @RequestMapping("/updateRoleFunc")
    public String updateRoleFunc(Model model,String roleId,String authType,String roleName,String enabled){
       
		try {
			roleName = URLDecoder.decode(roleName,"UTF-8");
			model.addAttribute("roleName",roleName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        model.addAttribute("authType",authType);
        model.addAttribute("roleId",roleId);
        model.addAttribute("enabled",enabled);
        //查询角色权限
        List<SysRoleFunc> roleFunList = roleFuncRepository.findByIdAndAuth(roleId,authType);
        List<String> rfList  = new ArrayList<>();;
        if(null != roleFunList && roleFunList.size() > 0){
            for(SysRoleFunc srf : roleFunList){
                 rfList.add(srf.getFuncCode());
             }
        }
        model.addAttribute("rfList",rfList);
        //查询所属权限
        List<SysFunction> topFunctions = roleService.findFuntionsByType(authType);
        model.addAttribute("topFunctions",topFunctions);

        return "role/updateRoleFunc";
    }

    /**w修改角色权限*/
    @RequestMapping("/saveUpdateRF")
    @ResponseBody
    public Map<String, Object> saveUpdateRF(Model model, String roleId,  String enabled, String funcStr ,String authType){
        Map<String, Object> map = new HashMap<>();
        map = roleService.saveUpdateRF(roleId, enabled,  funcStr , authType);
        return map;
    }
    /**查询权限下子级code*/
    @RequestMapping("/selectChildBox")
    @ResponseBody
    public Map<String, Object> selectChildBox(Model model, String funcode){
        Map<String, Object> map = new HashMap<>();
        String funcodes = roleService.selectChildBox(funcode);
        map.put("funcodes",funcodes);
        return map;
    }
}
