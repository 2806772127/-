package com.fb.goldencudgel.auditDecisionSystem.controller.role;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysRole;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.RoleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
/**
 * 角色控制类
 * @author David
 * 2018/12/28
 * */
@Controller
@RequestMapping("/role")
public class RoleController {

    private final Logger logger = LoggerFactory.getLogger(RoleController.class);
    private static final Integer PAGE_SIZE = 20;
    @Autowired
    private RoleServiceImpl roleService;


    /**角色查询页面**/
    @RequestMapping("/viewAllRoles")
    public String viewAllRoles(Model model){
        return "role/viewAllRoles";
    }

    /**角色查询结果**/
    @RequestMapping("/viewRoleList")
    public String viewRoleList(Model model,String roleName,@RequestParam(required = false, defaultValue = "1") Integer curPage){

        QueryPage<Object[]> pages = roleService.findByNameRoleType(roleName,new QueryPage<>(curPage, PAGE_SIZE) );
        model.addAttribute("page",pages);
        model.addAttribute("results",pages.getContent());
        model.addAttribute("roleName",roleName);
        return "role/viewRoleList";
    }
    /**儲存新增角色*/
    @RequestMapping("/saveAddRole")
    @ResponseBody
    public Map<String, Object> saveAddRole(Model model,String name,String id,String status,String orderNum){
        Map<String, Object> map = new HashMap<>();
        try{
            map = roleService.saveAddRole(name,id,status,orderNum);
        }catch (Exception e){
            map.put("flag",false);
            map.put("resultMsg","網路異常，儲存失敗");
        }
        return map;
    }

    /**儲存修改角色*/
    @RequestMapping("/saveEditRole")
    @ResponseBody
    public Map<String, Object> saveEditRole(Model model,String name,String id,String status,String orderNum){
        Map<String, Object> map = new HashMap<>();
        try{
            map = roleService.saveEditRole(name,id,status,orderNum);
        }catch (Exception e){
            map.put("flag",false);
            map.put("resultMsg","網路異常，修改失敗");
        }
        return map;
    }

    /**儲存修改角色*/
    @RequestMapping("/deleteRole")
    @ResponseBody
    public Map<String, Object> deleteRole(Model model,String id){
        Map<String, Object> map = new HashMap<>();
        try{
            QueryPage<Object[]> users = roleService.isExsitRoleUse(id);
            if(users.getContent().size()>0){
                map.put("flag",false);
                map.put("resultMsg","刪除失敗,角色已被使用，請先刪除相關用戶角色");
            }else{
                roleService.deleteRole(id);
                map.put("flag",true);
                map.put("resultMsg","刪除成功");
            }

        }catch (Exception e){
            map.put("flag",false);
            map.put("resultMsg","網路異常，刪除失敗");
        }
        return map;
    }

}
