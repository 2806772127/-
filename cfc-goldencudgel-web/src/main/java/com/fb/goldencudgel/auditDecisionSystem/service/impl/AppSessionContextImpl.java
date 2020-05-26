/**
 * Created By: Jms Project Team Created Date: Feb 25, 2011
 */
package com.fb.goldencudgel.auditDecisionSystem.service.impl;

import com.fb.goldencudgel.auditDecisionSystem.model.User;
import com.fb.goldencudgel.auditDecisionSystem.service.IAppSessionContext;
import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.comwave.core.context.impl.SessionContextImpl;


/**
 * The Class AppSessionContextImpl.
 * 
 * @author ribin
 * @version 1.0
 */
@Service("sessionContext")
public class AppSessionContextImpl extends SessionContextImpl implements IAppSessionContext {

  /** The Constant logger. */
  private static final Logger logger = LoggerFactory.getLogger(AppSessionContextImpl.class);

  @Override
  public User getUser() {

    return (User) getResource(UserUtil.SESSION_USER);
  }

}
