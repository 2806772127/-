/**
 * Created By: Comwave Project Team Created Date: Feb 24, 2011
 */
package com.fb.goldencudgel.auditDecisionSystem.Interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.comwave.core.context.ISessionContext;
import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.service.IAppSessionContext;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;


/**
 * The Class ContextInterceptor.
 * 
 * @author keven
 * @author ribin
 * @version 1.0
 */
public class ContextInterceptor implements HandlerInterceptor {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(ContextInterceptor.class);

  /** The Constant BIND_FLAG. */
  private static final String BIND_FLAG = "_BIND_FLAG_";

  /** The Constant BIND_VALUE. */
  private static final String BIND_VALUE = "_BIND_VALUE_";

  @Autowired
  @Resource(name = "sessionContext")
  protected IAppSessionContext sessionContext;

  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    logger.debug("begin to execute SessionContextInterceptor");
    HttpSession session = request.getSession();
    HttpSession sessiontim = request.getSession(false);
    
    boolean isAjaxRequest = false;
    if (request.getHeader("x-requested-with") != null && !"".equals(request.getHeader("x-requested-with"))
            && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
        isAjaxRequest = true;
    }
    if (isAjaxRequest && sessiontim == null){
        response.setHeader("sessionstatus", "timeout");
        return false;
    }
    
    String flag = (String) request.getAttribute(BIND_FLAG);
    if (BIND_VALUE.equals(flag))
      return true;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return true;
    }
    if (authentication instanceof AnonymousAuthenticationToken) {
      return true;
    }
    User user =
        (User) (authentication.getPrincipal() == null ? null : authentication.getPrincipal());
    String userId = user.getUsername() == null ? null : user.getUsername();
    String ip = request.getRemoteAddr();
    String hostName = (String) session.getAttribute(ISessionContext.REMOTE_HOST_NAME);

    try {
      sessionContext.bindResource(ISessionContext.REMOTE_HOST_ADDRESS, ip);
      sessionContext.bindResource(ISessionContext.REMOTE_HOST_NAME, hostName);
      sessionContext.bindResource(ISessionContext.USERID, userId);
      sessionContext.bindResource(UserUtil.SESSION_USER, user);
      sessionContext.bindResource(ISessionContext.LOCALE, request.getLocale());
      request.setAttribute("userUtil",new UserUtil());
      request.setAttribute(BIND_FLAG, BIND_VALUE);
      session.setAttribute("user",user);
      session.setMaxInactiveInterval(43200);
      session.setAttribute("lastRequestTime", System.currentTimeMillis());
      return true;
    } finally {

    }
  }

  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    try {
      sessionContext.unbindResource(ISessionContext.REMOTE_HOST_ADDRESS);
      sessionContext.unbindResource(ISessionContext.REMOTE_HOST_NAME);
      sessionContext.unbindResource(ISessionContext.USERID);
      sessionContext.unbindResource(UserUtil.SESSION_USER);
      sessionContext.unbindResource(ISessionContext.LOCALE);

      request.setAttribute(BIND_FLAG, "");
    } catch (Throwable e) {
      logger.error(e.getMessage(), e);
    }
  }

  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    try {
      sessionContext.unbindResource(ISessionContext.REMOTE_HOST_ADDRESS);
      sessionContext.unbindResource(ISessionContext.REMOTE_HOST_NAME);
      sessionContext.unbindResource(ISessionContext.USERID);
      sessionContext.unbindResource(UserUtil.SESSION_USER);
      sessionContext.unbindResource(ISessionContext.LOCALE);
      request.setAttribute(BIND_FLAG, "");
      HttpSession session = request.getSession(true);
    } catch (Throwable e) {
      logger.error(e.getMessage(), e);
    }
  }

}
