package com.fb.goldencudgel.auditDecisionSystem.domain.applyIncom;

import com.fb.goldencudgel.auditDecisionSystem.domain.FbCommonDBPK;
import org.hibernate.validator.constraints.EAN;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name = "FB_APPLY_INCOM")
public class FbApplyIncom implements Serializable {

  private static final long serialVersionUID = 1L;


  @Id
  @Column(name = "BUSINESS_ID")
  private String businessId;

  @Column(name = "COMPILATION_NO")
  private String compilationNo;

  @Column(name = "TRAND_ID")
  private String trandId;

  @Column(name = "CUSTOMER_NAME")
  private String customerName;

  @Column(name = "COM_INDUSTRY_CODE")
  private String comIndustryCode;

  @Column(name = "APPLY_DATE")
  private java.sql.Timestamp applyDate;

  @Column(name = "APPLY_USER")
  private String applyUser;

  @Column(name = "APPLY_NUMBER")
  private String applyNumber;

  @Column(name = "CREATE_USER")
  private String createUser;

  @Column(name = "CREATE_TIME")
  private java.sql.Timestamp createTime;

  @Column(name = "UPDATE_USER")
  private String updateUser;

  @Column(name = "UPDATE_TIME")
  private java.sql.Timestamp updateTime;




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


  public java.sql.Timestamp getApplyDate() {
    return applyDate;
  }

  public void setApplyDate(java.sql.Timestamp applyDate) {
    this.applyDate = applyDate;
  }


  public String getApplyUser() {
    return applyUser;
  }

  public void setApplyUser(String applyUser) {
    this.applyUser = applyUser;
  }


  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }


  public java.sql.Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.sql.Timestamp createTime) {
    this.createTime = createTime;
  }


  public String getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }


  public java.sql.Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(java.sql.Timestamp updateTime) {
    this.updateTime = updateTime;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getComIndustryCode() {
    return comIndustryCode;
  }

  public void setComIndustryCode(String comIndustryCode) {
    this.comIndustryCode = comIndustryCode;
  }

  public String getBusinessId() {
    return businessId;
  }

  public void setBusinessId(String businessId) {
    this.businessId = businessId;
  }

  public String getApplyNumber() {
    return applyNumber;
  }

  public void setApplyNumber(String applyNumber) {
    this.applyNumber = applyNumber;
  }
}
