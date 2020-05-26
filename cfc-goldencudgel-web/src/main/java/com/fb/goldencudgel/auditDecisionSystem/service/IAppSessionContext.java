
package com.fb.goldencudgel.auditDecisionSystem.service;

import com.comwave.core.context.ISessionContext;

import com.fb.goldencudgel.auditDecisionSystem.model.User;


/**
 * The Interface IAppSessionContext.
 * 
 * @author ribin
 * @version 1.0
 */
public interface IAppSessionContext extends ISessionContext {
  public User getUser();

}
