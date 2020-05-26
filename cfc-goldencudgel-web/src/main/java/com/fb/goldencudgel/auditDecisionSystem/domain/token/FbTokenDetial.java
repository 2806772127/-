package com.fb.goldencudgel.auditDecisionSystem.domain.token;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * TOKEN详情表
 */
@Entity
@Table(name = "FB_TOKEN_DETIAL")
public class FbTokenDetial implements Serializable {

  private static final long serialVersionUID = 1L;

  //用户唯一ID
  @Id
  @Column(name ="USER_UID")
  private String userUid;

  //TOKEN
  @Column(name ="TOKEN")
  private String token;

  //有效期类型 Y年 M月 D天 H小时 M分 S秒
  @Column(name ="TOKEN_VALID_TYPE")
  private String tokenValidType;

  //有效期值
  @Column(name ="TOKEN_VALID")
  private double tokenValid;

  //最后一次登录时间
  @Column(name ="LOGIN_TIME")
  private Date loginTime;

  //创建时间
  @Column(name ="CREATE_TIME")
  private Date createTime;

  //登录类型 1密码登录 2免密登录
  @Column(name ="LOGIN_TYPE")
  private String loginType;

  //客户类型 M:经理，C:组长，S:业务员
  @Column(name ="USER_TYPE")
  private String userType;

  //登录失败次数
  @Column(name ="LOGIN_DEFAULT_COUNT")
  private double loginDefaultCount;

  //最后一次操作时间
  @Column(name ="LAST_OPRATOR_TIME")
  private java.sql.Timestamp lastOpratorTime;


  public String getUserUid() {
    return userUid;
  }

  public void setUserUid(String userUid) {
    this.userUid = userUid;
  }


  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  public String getTokenValidType() {
    return tokenValidType;
  }

  public void setTokenValidType(String tokenValidType) {
    this.tokenValidType = tokenValidType;
  }


  public double getTokenValid() {
    return tokenValid;
  }

  public void setTokenValid(double tokenValid) {
    this.tokenValid = tokenValid;
  }


  public Date getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(Date loginTime) {
    this.loginTime = loginTime;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


  public String getLoginType() {
    return loginType;
  }

  public void setLoginType(String loginType) {
    this.loginType = loginType;
  }


  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }


  public double getLoginDefaultCount() {
    return loginDefaultCount;
  }

  public void setLoginDefaultCount(double loginDefaultCount) {
    this.loginDefaultCount = loginDefaultCount;
  }


  public java.sql.Timestamp getLastOpratorTime() {
    return lastOpratorTime;
  }

  public void setLastOpratorTime(java.sql.Timestamp lastOpratorTime) {
    this.lastOpratorTime = lastOpratorTime;
  }

}
