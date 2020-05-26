package com.fb.goldencudgel.auditDecisionSystem.domain.creditReport;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "FB_CREDIT_REPORTING")
public class FbCreditReporting {

  @Id
  @Column(name = "REPORTING_ID")
  private String reportingId;
  @Column(name = "COMPILATION_NO")
  private String compilationNo;
  @Column(name = "TRAND_ID")
  private String trandId;
  @Column(name = "VISITING_TIME")
  private java.sql.Date visitingTime;
  @Column(name = "VISITING_USER")
  private String visitingUser;
  @Column(name = "COMPILATION_NAME")
  private String compilationName;
  @Column(name = "CREATE_TIME")
  private java.sql.Date createTime;
  @Column(name = "CREATE_USER")
  private String createUser;
  @Column(name = "INDUSTRY_TYPE")
  private String industryType;
  /** 打卡历史ID**/
  @Column(name = "PUNCH_CARD_RECODE_ID")
  private String punchCardRecodeId;
  /** 案件來源：01:官网 02：自行开发 03：展期 04：人寿转介 05：产险转介 60：行员转介 99：其他**/
  @Column(name = "ENTER_SOURCE")
  private String enterSource;
  /** 公司登記地址**/
  @Column(name = "COMPANY_ADDRESS")
  private String companyAddress;
  /** 實際營業地址**/
  @Column(name = "BUSINESS_ADDRESS")
  private String businessAddress;
  /** 拜访备注**/
  @Column(name = "VISIT_DESC")
  private String visitDesc;
  /** 拜访对象**/
  @Column(name = "VISIT_NAME")
  private String visitName;
  /** 职称**/
  @Column(name = "VISIT_POSITION")
  private String visitPosition;
  /** 预定拜访时间**/
  @Column(name = "RESERVE_VISIT_TIME")
  private java.sql.Timestamp reserveVisitTime;


  public String getReportingId() {
    return reportingId;
  }

  public void setReportingId(String reportingId) {
    this.reportingId = reportingId;
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


  public java.sql.Date getVisitingTime() {
    return visitingTime;
  }

  public void setVisitingTime(java.sql.Date visitingTime) {
    this.visitingTime = visitingTime;
  }


  public String getVisitingUser() {
    return visitingUser;
  }

  public void setVisitingUser(String visitingUser) {
    this.visitingUser = visitingUser;
  }


  public String getCompilationName() {
    return compilationName;
  }

  public void setCompilationName(String compilationName) {
    this.compilationName = compilationName;
  }


  public java.sql.Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.sql.Date createTime) {
    this.createTime = createTime;
  }


  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }


  public String getIndustryType() {
    return industryType;
  }

  public void setIndustryType(String industryType) {
    this.industryType = industryType;
  }

  public String getPunchCardRecodeId() {
    return punchCardRecodeId;
  }

  public void setPunchCardRecodeId(String punchCardRecodeId) {
    this.punchCardRecodeId = punchCardRecodeId;
  }

  public String getEnterSource() {
    return enterSource;
  }

  public void setEnterSource(String enterSource) {
    this.enterSource = enterSource;
  }

  public String getCompanyAddress() {
    return companyAddress;
  }

  public void setCompanyAddress(String companyAddress) {
    this.companyAddress = companyAddress;
  }

  public String getBusinessAddress() {
    return businessAddress;
  }

  public void setBusinessAddress(String businessAddress) {
    this.businessAddress = businessAddress;
  }

  public String getVisitDesc() {
    return visitDesc;
  }

  public void setVisitDesc(String visitDesc) {
    this.visitDesc = visitDesc;
  }

  public String getVisitName() {
    return visitName;
  }

  public void setVisitName(String visitName) {
    this.visitName = visitName;
  }

  public String getVisitPosition() {
    return visitPosition;
  }

  public void setVisitPosition(String visitPosition) {
    this.visitPosition = visitPosition;
  }

  public Timestamp getReserveVisitTime() {
    return reserveVisitTime;
  }

  public void setReserveVisitTime(Timestamp reserveVisitTime) {
    this.reserveVisitTime = reserveVisitTime;
  }
}
