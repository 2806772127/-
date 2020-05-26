package com.fb.goldencudgel.auditDecisionSystem.domain.fbWebexConfig;




import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "FB_WEBEX_CONFIG")
public class FbWebexConfig {
  @Id
  @Column(name = "ACCOUNT")
  private String account;
  @Column(name = "ACCOUNT_NAME")
  private String accountName;
  @Column(name = "PWD")
  private String pwd;
  @Column(name = "SITE")
  private String site;
  @Column(name = "URL")
  private String url;
  @Column(name = "IS_USED")
  private String isUser;
  @Column(name = "USE_START_TIME")
  private java.sql.Timestamp startTime;
  @Column(name = "USE_END_TIME")
  private java.sql.Timestamp endTime;

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public String getSite() {
    return site;
  }

  public void setSite(String site) {
    this.site = site;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getIsUser() {
    return isUser;
  }

  public void setIsUser(String isUser) {
    this.isUser = isUser;
  }

  public Timestamp getStartTime() {
    return startTime;
  }

  public void setStartTime(Timestamp startTime) {
    this.startTime = startTime;
  }

  public Timestamp getEndTime() {
    return endTime;
  }

  public void setEndTime(Timestamp endTime) {
    this.endTime = endTime;
  }
}
