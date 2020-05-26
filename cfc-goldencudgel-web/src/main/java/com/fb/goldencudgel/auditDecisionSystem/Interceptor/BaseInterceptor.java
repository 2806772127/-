/**
 * Created By: Comwave Project Team Created Date: Mar 1, 2011
 */
package com.fb.goldencudgel.auditDecisionSystem.Interceptor;

import javax.annotation.Resource;

import com.fb.goldencudgel.auditDecisionSystem.service.IAppSessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import com.comwave.core.context.ISessionContext;


/**
 * The Class BaseInterceptor.
 * 
 * @author ribin
 * @version 1.0
 */
public abstract class BaseInterceptor implements HandlerInterceptor {

  /** The session context. */
  @Autowired
  @Resource(name = "sessionContext")
  protected IAppSessionContext sessionContext;

  protected String getUserId() {
    return sessionContext.getUserId();
  }

}
