/**
 * Created By: Jms Project Team
 * Created Date: Dec 27, 2015
 */
package com.fb.goldencudgel.auditDecisionSystem.Interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Class LoginSessionCheckIntercepter.
 */
public class LoginSessionCheckIntercepter extends BaseInterceptor {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(LoginSessionCheckIntercepter.class);

  /**
   * 完成对页面的render以后调用
   */
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    // TODO Auto-generated method stub

  }

  /**
   * 完成具体方法之后调用
   */
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    // TODO Auto-generated method stub

  }

  /**
   * 调用controller具体方法之前调用
   */
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String[] noFilters = new String[] {"/login", "/images/", "/js/", "/css/", "/files/"};
    String contextPath = request.getContextPath();
    String requestURI = request.getRequestURI();
    HttpSession session = request.getSession();
    String path = requestURI.replaceFirst(contextPath, "");
    boolean beFilter = true;
    if (path.equals("/")) {
      beFilter = false;
    } else {
      for (String s : noFilters) {
        if (path.indexOf(s) != -1) {
          beFilter = false;
          break;
        }
      }
    }
    boolean flag = true;
    if (beFilter) {
      // 获取最后检查时间
      Long sessionLastCheckTime = (Long) session.getAttribute("sessionLastCheckTime");
      if (sessionLastCheckTime != null) {
        long currentTime = System.currentTimeMillis();
        // 如果超过10秒则检查用户登录记录
        if (currentTime - sessionLastCheckTime > 10000) {
          flag = isSessionValid(request, response);
        }
      } else {
        flag = isSessionValid(request, response);
      }
      // 返回false，该用户已在另一设备登录，强制退出
      if (!flag) {
        logger.info("强制退出：" + UserUtil.getCurrUser().getUsername()+ "," + request.getRemoteAddr());
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter pw = response.getWriter();
        session.setAttribute("isForceLoginout", "true");
        pw.write("<script>alert('该用户在另一设备登录，已强制退出！');window.opener=null;window.open('','_top');window.close();</script>");
        pw.close();
      }
    }
    return flag;
  }

  /**
   * 根据用户登录名和用户登录IP查询用户登录记录，有记录则返回true，否则返回false.
   * 
   * @param request the request
   * @param response the response
   * @return true, if is session valid
   */
  private boolean isSessionValid(HttpServletRequest request, HttpServletResponse response) {
    User user = UserUtil.getCurrUser();
    boolean flag = true;
    if (user != null) {
//      String loginIp = request.getRemoteAddr();
//      flag = loginSessionService.findByUserIdAndLoginIp(loginIp);
//      if (flag) {
        HttpSession session = request.getSession();
        session.setAttribute("sessionLastCheckTime", System.currentTimeMillis());
//      }
    }
    return flag;
  }
}
