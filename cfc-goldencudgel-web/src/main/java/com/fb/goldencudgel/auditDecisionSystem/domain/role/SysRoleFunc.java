package com.fb.goldencudgel.auditDecisionSystem.domain.role;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  角色权限表
 * @author  David
 *
 * */
@Entity
@Table(name = "SYS_ROLE_FUNC")
@IdClass(SysRoleFuncPK.class)
public class SysRoleFunc implements Serializable {
  @Id
  @Column(name = "ROLE_ID")
  private String roleId;
  @Id
  @Column(name = "FUNC_CODE")
  private String funcCode;
  /**是否啟用 1:启用 0：禁用*/
  @Column(name = "IS_ENABLED")
  private String status;
  /**是權限類型 1：APP 2:WEB*/
  @Column(name = "AUTH_TYPE")
  private String authType;

  public String getAuthType() {
    return authType;
  }

  public void setAuthType(String authType) {
    this.authType = authType;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }


  public String getFuncCode() {
    return funcCode;
  }

  public void setFuncCode(String funcCode) {
    this.funcCode = funcCode;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
