package com.fb.goldencudgel.auditDecisionSystem.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.comwave.core.data.domain.QueryPage;
import com.fb.goldencudgel.auditDecisionSystem.domain.message.MessageReceive;
import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysRole;
import com.fb.goldencudgel.auditDecisionSystem.domain.token.FbTokenDetial;
import com.fb.goldencudgel.auditDecisionSystem.domain.user.FbUser;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.repository.AuditConfigurationRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.MessageReceiveRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.SysRoleRepository;
import com.fb.goldencudgel.auditDecisionSystem.repository.UserRepository;
import com.fb.goldencudgel.auditDecisionSystem.schema.getUserProfile.GetUserProfileRq;
import com.fb.goldencudgel.auditDecisionSystem.schema.getUserProfile.GetUserProfileRs;
import com.fb.goldencudgel.auditDecisionSystem.service.IInterfaceService;
import com.fb.goldencudgel.auditDecisionSystem.service.IndexService;
import com.fb.goldencudgel.auditDecisionSystem.service.impl.MessageServiceImpl;


/**
 * @Auther hu
 */
@Controller
public class IndexController {

    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IndexService indexService;
    
    @Autowired
    private IInterfaceService interfaceService;
    
    @Autowired
	private MessageServiceImpl messageService;
    
    @Autowired
    private AuditConfigurationRepository auditConfigurationRepository;

    @Autowired
    private MessageReceiveRepository messageReceiveRepository;
    
    @Autowired
    private SysRoleRepository sysRoleRepository;

    @RequestMapping("/index")
    public String index(Model model){
        return "index/index";
    }

    @RequestMapping("/login")
    public String login(Model model){
        return "login";
    }
    @RequestMapping("/hasError")
    public String hasError(Model model){
        return "error1";
    }

    @RequestMapping("/logout")
    public String logout(Model model,HttpServletRequest request) {
        SecurityContext sc = (SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        sc.setAuthentication(null);
        HttpSession session = request.getSession();
        session.invalidate();
        return "login";
    }

    @RequestMapping(value = "/loginTest")
    public String loginTest(Model model, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        HttpSession session = request.getSession(true);

        String userCode = request.getParameter("userCode");//登入員編
        if(StringUtils.isBlank(userCode)) {
            return "login";
        }

        User user = new User();
        FbUser fbUser = userRepository.findByUserCode(userCode);

        if (fbUser == null) {
            return "login";
        }

        BeanUtils.copyProperties(fbUser, user);

        // 获取token
        FbTokenDetial tokenDetial = indexService.updateToken(fbUser.getUserUid());

        // 登录类型
        user.setLoginType("2");
        user.setTokenDetial(tokenDetial);

        // 判断用户角色
        List<Object> sysRoleList = indexService.findHasFunction(new SysRole().getRoleId(fbUser.getRoleId()),userCode);
        user.setRoleStringList(sysRoleList);

        //更新消息未读数
        user=updateMessageCount(user);

        // 更新用户信息
        BeanUtils.copyProperties(user, fbUser);
        userRepository.saveAndFlush(fbUser);
        Authentication authentication = buildAuthentication(user);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        session.setAttribute("messageCount", user.getMessageCount()==null?0:user.getMessageCount());

        //通过readTime判断查看咨询结果数据里是否已全部已读取
        QueryPage<Object[]> pageContent = messageService.findByIsHasReadTime(new QueryPage<Object[]>(1, 20));
        Object contentCount;
        if(pageContent.getContent().isEmpty()) {
            contentCount =0;
        }else{
            contentCount = pageContent.getContent().get(0);
           }
           session.setAttribute("contentCount", contentCount);
        try {
            response.sendRedirect("missionStroke/viewMissionStroke");
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    @RequestMapping(value = "/thirdPartLogin", method = RequestMethod.POST)
    public String thirdPartLogin(Model model,HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession(true);

        String strUsrID = request.getParameter("strUsrID");//登入員編
        if(StringUtils.isBlank(strUsrID)) {
            return "login";
        }
        
        User user = new User();
        FbUser fbUser = userRepository.findByUserCode(strUsrID);
        
        GetUserProfileRq getUserProfileRq = new GetUserProfileRq();
        getUserProfileRq.setUserCode(strUsrID);
        GetUserProfileRs getUserProfileRs = interfaceService.getUserProfile(getUserProfileRq);
        if (StringUtils.isBlank(getUserProfileRs.getRoleId())) {
            return "login";
        }
        if (fbUser == null) {
            fbUser = new FbUser();
            String userUid = UUID.randomUUID().toString();
            fbUser.setUserUid(userUid);
            
            fbUser.setAccount(getUserProfileRs.getAccount());
            fbUser.setLoginType("2");
            fbUser.setMessageCount(0);
            fbUser.setUserCompany("台北富邦銀行");
        }
        
        fbUser.setUserName(getUserProfileRs.getUserName());
        fbUser.setUserArea(getUserProfileRs.getAreaCode());
        fbUser.setUserAreaName(getUserProfileRs.getAreaName());
        fbUser.setUserGroup(getUserProfileRs.getGroupCode());
        fbUser.setUserGroupName(getUserProfileRs.getGroupName());
        fbUser.setRoleId(getUserProfileRs.getRoleId());
        fbUser.setUserType(getUserProfileRs.getUserType());
        
        BeanUtils.copyProperties(fbUser, user);
        
        logger.info("third part login user id : " + strUsrID);
        // 获取token
        FbTokenDetial tokenDetial = indexService.updateToken(fbUser.getUserUid());
        
        user.setUserCode(strUsrID);
        // 登录类型
        user.setLoginType("2");
        user.setTokenDetial(tokenDetial);
        
        // 判断用户角色
        List<Object> sysRoleList  =  indexService.findHasFunction(getUserProfileRs.getRoleId(),strUsrID);
        user.setRoleStringList(sysRoleList);

        //更新消息未读数
        user=updateMessageCount(user);

        // 更新用户信息
        BeanUtils.copyProperties(user, fbUser);
        userRepository.saveAndFlush(fbUser);
        Authentication authentication = buildAuthentication(user);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        session.setAttribute("messageCount", user.getMessageCount()==null?0:user.getMessageCount());
        
        //通过readTime判断查看咨询结果数据里是否已全部已读取
        QueryPage<Object[]> pageContent = messageService.findByIsHasReadTime(new QueryPage<Object[]>(1, 20));
		   Object contentCount;
		   if(pageContent.getContent().isEmpty()) {
			    contentCount =0;
		   }else{
		    contentCount = pageContent.getContent().get(0);
		   }
		   session.setAttribute("contentCount", contentCount);
        try {
            response.sendRedirect("missionStroke/viewMissionStroke");
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    @RequestMapping(value = "/it")
    public String loginForIt(Model model, HttpServletRequest request, HttpServletResponse response) {
        
        // 判斷有沒有IT角色，角色沒配置，不能登錄
        SysRole sysRole = sysRoleRepository.findRoleById("CMLF_IT_USR");
        if (sysRole == null) {
            return "login";
        }
        HttpSession session = request.getSession(true);
        User user = new User();
        FbUser fbUser = userRepository.findByUserCode("IT");
        if (fbUser == null) {
            fbUser = new FbUser();
            String userUid = UUID.randomUUID().toString();
            fbUser.setUserCode("IT");
            fbUser.setUserUid(userUid);
            fbUser.setAccount("IT");
            fbUser.setLoginType("2");
            fbUser.setMessageCount(0);
            fbUser.setUserCompany("台北富邦銀行");
        }
        
        fbUser.setUserName("IT");
        fbUser.setUserArea("");
        fbUser.setUserAreaName("");
        fbUser.setUserGroup("");
        fbUser.setUserGroupName("");
        fbUser.setRoleId("CMLF_IT_USR");
        fbUser.setUserType("I");
        
        BeanUtils.copyProperties(fbUser, user);
        
        // 获取token
        FbTokenDetial tokenDetial = indexService.updateToken(fbUser.getUserUid());
        
        // 登录类型
        user.setLoginType("2");
        user.setTokenDetial(tokenDetial);
        
        // 判断用户角色
        List<Object> sysRoleList  =  indexService.findHasFunction(fbUser.getRoleId(), "IT");
        user.setRoleStringList(sysRoleList);

        // 更新用户信息
        BeanUtils.copyProperties(user, fbUser);
        userRepository.saveAndFlush(fbUser);
        Authentication authentication = buildAuthentication(user);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        session.setAttribute("messageCount", user.getMessageCount()==null?0:user.getMessageCount());
        try {
            response.sendRedirect("systemData/indexForIT");
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public User updateMessageCount(User user) {
        List<MessageReceive> messageReceiveList = messageReceiveRepository.getUnreadMessage(user.getUserCode());
        int count = messageReceiveList == null ? 0 : messageReceiveList.size();
        user.setMessageCount(count);
        return user;
    }
    
    /**
     * 国际化
     * @param request
     * @param response
     * @param lang
     * @return
     */
    @RequestMapping("/changeLanauage")
    public String changeSessionLanauage(HttpServletRequest request, HttpServletResponse response, String lang){

        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if("2".equals(lang))
            localeResolver.setLocale(request, response, new Locale("zh","CN"));
        else if("3".equals(lang))
            localeResolver.setLocale(request, response, new Locale("en","US"));

        return "index/index";
    }

    private Authentication buildAuthentication(final User user) {
        Authentication authentication = new Authentication() {
            boolean authenticated = true;
            @Override
            public String getName() {
                return user.getUsername();
            }
            @Override
            public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
                this.authenticated = authenticated;
            }

            @Override
            public boolean isAuthenticated() {
                return authenticated;
            }

            @Override
            public Object getPrincipal() {
                return user;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return user;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getAuthorities();
            }
        };

        return authentication;
    }
    
    public String getRoleCode(String roleId) {
        String roleCode = "";
        if (roleId == null) {
            roleId = "";
        }
        if (roleId.contains("CMLF_TEA")) {
            roleCode = "C";
        } else if (roleId.contains("CMLF_CRE")) {
            roleCode = "Z";
        } else if (roleId.contains("CMLF_SAL")) {
            roleCode = "S";
        } else if (roleId.contains("CMLF_DIS")) {
            roleCode = "A";
        } else if (roleId.contains("CMLF_SUP")) {
            roleCode = "M";
        } else {
            roleCode = "";
        }
        return roleCode;
    }
}
