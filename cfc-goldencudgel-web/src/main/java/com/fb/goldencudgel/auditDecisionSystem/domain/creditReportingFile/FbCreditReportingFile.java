package com.fb.goldencudgel.auditDecisionSystem.domain.creditReportingFile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "FB_CREDIT_REPORTING_FILE")
public class FbCreditReportingFile {

  @Id
  @Column(name = "ATTACH_ID")
  private String attachId;
  @Column(name = "COMPILATION_NO")
  private String compilationNo;
  @Column(name = "TRAND_ID")
  private String trandId;
  @Column(name = "COMPILATION_NAME")
  private String compilationName;
  @Column(name = "CREATE_TIME")
  private Date createTime;
  @Column(name = "CREATE_USER")
  private String createUser;
  /**节点 1:拜訪筆記;2:進件申請;3:征信實訪;4:聯征同意書*/
  @Column(name = "NODE_CODE")
  private String nodeCode;

  public String getNodeCode() {
    return nodeCode;
  }

  public void setNodeCode(String nodeCode) {
    this.nodeCode = nodeCode;
  }

  public String getAttachId() {
    return attachId;
  }

  public void setAttachId(String attachId) {
    this.attachId = attachId;
  }


  public String getCompilationNo() {
    return compilationNo;
  }

  public void setCompilationNo(String compilationNo) {
    this.compilationNo = compilationNo;
  }


  public String getTrandId() {
    return trandId;
  }

  public void setTrandId(String trandId) {
    this.trandId = trandId;
  }


  public String getCompilationName() {
    return compilationName;
  }

  public void setCompilationName(String compilationName) {
    this.compilationName = compilationName;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

}
