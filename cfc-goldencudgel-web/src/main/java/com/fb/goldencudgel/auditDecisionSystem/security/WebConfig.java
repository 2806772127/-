/**
 * Created By: Jms Project Team Created Date: Feb 24, 2016
 */
package com.fb.goldencudgel.auditDecisionSystem.security;

import java.util.HashMap;
import java.util.Map;

import com.fb.goldencudgel.auditDecisionSystem.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.comwave.core.utils.MathUtil;
import com.comwave.core.velocity.VelocityTool;

/**
 * @author Geln Yang
 * @version 1.0
 */
@Configuration
public class WebConfig {

  @Autowired
  protected Environment environment;
  /** The Constant CONFIGKEY_HOTDEPLOY_STATUS. */
  private static final String CONFIGKEY_HOTDEPLOY_STATUS = "ems.queue.bpm.ruleeng.status";

//  @Bean(name = "WebContextInterceptor")
//  public HandlerInterceptor getWebContextInterceptor() {
//    return new WebContextInterceptor();
//  }

  @Bean
  public UserUtil getUserUtils() {
    return new UserUtil();
  }

  protected Map<String, Object> getVelocityAttributeMap() {
    Map<String, Object> attributes = new HashMap<String, Object>();
    attributes.put("userUtils", getUserUtils());
    attributes.put("mathUtil", new MathUtil());
    attributes.put("velocityTool", new VelocityTool());
    return attributes;
  }
}
