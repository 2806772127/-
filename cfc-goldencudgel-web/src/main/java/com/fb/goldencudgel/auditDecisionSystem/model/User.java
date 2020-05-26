/**
 * 
 */
package com.fb.goldencudgel.auditDecisionSystem.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysFunction;
import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysRole;
import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysRoleFunc;
import com.fb.goldencudgel.auditDecisionSystem.domain.token.FbTokenDetial;
import com.fb.goldencudgel.auditDecisionSystem.domain.user.FbUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * The Class User.
 * 
 * @author Neal
 * @version 1.0
 * @description 用户类
 */
public class User extends FbUser implements UserDetails, Validator {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** 经理. */
  public static final String USER_TYPE_M = "M";

  /** 处长. */
  public static final String USER_TYPE_A = "A";

  /** 组长. */
  public static final String USER_TYPE_C = "C";

  /** 业务员. */
  public static final String USER_TYPE_S = "S";

  /** 征信员. */
  public static final String USER_TYPE_Z = "Z";

  /** 角色 *. */
  private List<SysRole> roleList = new ArrayList<SysRole>();

  private List<Object> roleStringList = new ArrayList<>();
  /** 登陆时间. */
  private Long loginTime;
  /*------------functions--------------------------------------------------*/

  /**
   * Checks for function.
   * 
   * @param functions the functions
   * @return true, if successful
   */
//  public boolean hasStringFunction(SysRoleFunc... functions) {
//    for (SysRoleFunc function : functions) {
//      // Role role = getRole(currRoleId);//同一人员所有角色权限合并
//      for (SysRole role : getRoleList()) {
//        if (role.getFunctions().contains(function)) {
//          return true;
//        }
//      }
//    }
//    return false;
//  }

  public boolean hasFunction(SysRoleFunc... functions){
    for(SysRoleFunc function : functions){
      for(Object role : getRoleStringList()){
        if(role != null && role.toString().equals(functions)){
          return true;
        }
      }
    }
    return false;
  }



  /**
   * Checks for function code.
   * 
   * @param funcCode the func code
   * @return true, if successful
   */
//  public boolean hasStringFunction(String... funcCode) {
//    // Role role = getRole(currRoleId);//同一人员所有角色权限合并
//    for (String code : funcCode) {
//      for (SysRole role : getRoleList()) {
//        List<SysFunction> functions = role.getFunctions();
//        if (functions == null || functions.size() == 0)
//          continue;
//        for (SysFunction function : functions) {
//          if (function.getCode().equalsIgnoreCase(code)) {
//            return true;
//          }
//        }
//      }
//    }
//    return false;
//  }
public boolean hasFunctionCode(String... funcCode){
    for(String code :funcCode){
      for(Object role:getRoleStringList()){
        if(role != null && role.toString().equals(code)){
          return true;
        }
      }
    }
    return false;
}


  /**
   * 获取roleId该用户的角色.
   * 
   * @param roleId 角色ID
   * @return the role
   */
  public SysRole getRole(String roleId) {
    if (getRoleList() != null && getRoleList().size() > 0) {
      for (SysRole role : getRoleList()) {
        if (role.getId().equals(roleId)) {
          return role;
        }
      }
    }
    return null;
}

  public boolean hasRole(String funcCode) {
    if (this.getRoleList() != null && this.getRoleList().size() > 0) {
      SysRole roel = this.getRoleList().get(0);
      for( SysFunction function : roel.getFunctions()) {
        if(function.equals(funcCode)) {
          return true;
        }
      }
    }
    return false;
  }

//  public List<String> getRoleId() {
//    List<String> roleIds = new ArrayList<String>();
//    if (getRoleList() != null && getRoleList().size() > 0) {
//      for (SysRole role : getRoleList()) {
//        if (StringUtils.isNoneBlank(role.getId()))
//          roleIds.add(role.getId());
//      }
//    }
//    return roleIds;
//  }

  /*-----------setter/getter---------------------------------------------------*/



  public List<SysRole> getRoleList() {
    return roleList;
  }

  public List<Object> getRoleStringList() {
    return roleStringList;
  }

  public void setRoleStringList(List<Object> roleStringList) {
    this.roleStringList = roleStringList;
  }

  public void setRoleList(List<SysRole> roleList) {
    this.roleList = roleList;
  }


  public Long getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(Long loginTime) {
    this.loginTime = loginTime;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.validation.Validator#supports(java.lang.Class)
   */
  @Override
  public boolean supports(Class<?> clazz) {
    return User.class.isAssignableFrom(clazz);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.validation.Validator#validate(java.lang.Object,
   * org.springframework.validation.Errors)
   */
  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "required", "user id can't null");
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails#getAuthorities ()
   */
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRoleList();
  }

  @Override
  public String getUsername() {
    return getUserName() == null ? null : getUserName().toLowerCase();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonExpired ()
   */
  public boolean isAccountNonExpired() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails#isAccountNonLocked ()
   */
  public boolean isAccountNonLocked() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails# isCredentialsNonExpired()
   */
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.springframework.security.core.userdetails.UserDetails#isEnabled()
   */
  public boolean isEnabled() {
    return true;
  }

}
