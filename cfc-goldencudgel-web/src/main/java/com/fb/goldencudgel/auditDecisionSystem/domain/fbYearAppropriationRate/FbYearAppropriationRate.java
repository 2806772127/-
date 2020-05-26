package com.fb.goldencudgel.auditDecisionSystem.domain.fbYearAppropriationRate;

import com.fb.goldencudgel.auditDecisionSystem.domain.FbYearPK;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FB_YEAR_APPROPRIATION_RATE")
@IdClass(FbYearPK.class)
public class FbYearAppropriationRate {

  @Id
  @Column(name = "USER_CUSTOMER_ID")
  private String userCustomerId;
  @Id
  @Column(name = "USER_CODE")
  private String userCode;
  @Id
  @Column(name = "RATE_DATE")
  private String rateDate;
  @Column(name = "COM_RATE_GOAL")
  private String comRateGoal;
  @Column(name = "CREDIT_RATE_GOAL")
  private String creditRateGoal;
  @Column(name = "HOUSE_RATE_GOAL")
  private String houseRateGoal;
  @Column(name = "CREATE_TIME")
  private Date createTime;


  public String getUserCustomerId() {
    return userCustomerId;
  }

  public void setUserCustomerId(String userCustomerId) {
    this.userCustomerId = userCustomerId;
  }


  public String getUserCode() {
    return userCode;
  }

  public void setUserCode(String userCode) {
    this.userCode = userCode;
  }


  public String getRateDate() {
    return rateDate;
  }

  public void setRateDate(String rateDate) {
    this.rateDate = rateDate;
  }


  public String getComRateGoal() {
    return comRateGoal;
  }

  public void setComRateGoal(String comRateGoal) {
    this.comRateGoal = comRateGoal;
  }


  public String getCreditRateGoal() {
    return creditRateGoal;
  }

  public void setCreditRateGoal(String creditRateGoal) {
    this.creditRateGoal = creditRateGoal;
  }


  public String getHouseRateGoal() {
    return houseRateGoal;
  }

  public void setHouseRateGoal(String houseRateGoal) {
    this.houseRateGoal = houseRateGoal;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

}
