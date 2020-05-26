package com.fb.goldencudgel.auditDecisionSystem.domain.LoanCompany;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 公司表
 *
 */
@Entity
@Table(name = "FB_LOAN_COMPANY")
public class FbLoanCompany implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "COMPILATION_NO")
  private String compilationNo;

  @Column(name = "COM_NAME")
  private String comName;

  @Column(name = "COM_ORGANIZATION")
  private String comOrganization;

  @Transient
  private String comOrganizationName;

  @Column(name = "COM_ESTABDATE")
  private Date comEstabdate;

  @Column(name = "COM_SCALE")
  private String comScale;

  @Column(name = "COM_STAFF_NUM")
  private String comStaffNum;

  @Column(name = "COM_TAXATION_CODE")
  private String comTaxationCode;

  @Column(name = "COM_INDUSTRY")
  private String comIndustry;

  @Column(name = "COM_CREDIT_INDUSTRY_CODE")
  private String comCreditIndustryCode;

  @Column(name = "COM_CREDIT_INDUSTRY_NAME")
  private String comCreditIndustryName;

  @Column(name = "COM_OBJECT")
  private String comObject;

  @Column(name = "COM_ACTUAL_CAPITAL")
  private String comActualCapital;

  @Column(name = "COM_ACTUAL_CAPITAL_DATE")
  private String comActualCapitalDate;

  @Column(name = "COM_PRE_YEAR_INCOME")
  private String comPreYearIncome;

  @Column(name = "COM_MAINPRODUCT")
  private String comMainproduct;

  @Column(name = "COM_BUSINESS_ITEM")
  private String comBusinessItem;

  @Column(name = "COM_RECYEAR_REVENUE_INTERVAL")
  private String comRecyearRevenueInterval;

  @Column(name = "COM_IS_LABORINSUR_NUMBER")
  private String comIsLaborinsurNumber;

  @Column(name = "COM_LABORINSUR_NUMBER")
  private String comLaborinsurNumber;

  @Column(name = "COM_RECYEAR_REVENUE_DATE")
  private String comRecyearRevenueDate;

  @Column(name = "COM_RECYEAR_REVENUE")
  private String comRecyearRevenue;

  @Column(name = "COM_REG_POST_OFFICE_CODE")
  private String comRegPostOfficeCode;

  @Column(name = "COM_REG_CITY_CODE")
  private String comRegCityCode;

  @Column(name = "COM_REG_DISTRICT_CODE")
  private String comRegDistrictCode;

  @Column(name = "COM_REG_STREET_CODE")
  private String comRegStreetCode;

  @Column(name = "COM_REG_TUNNEL")
  private String comRegTunnel;

  @Column(name = "COM_REG_LANE")
  private String comRegLane;

  @Column(name = "COM_REG_ADDNUMBER")
  private String comRegAddnumber;

  @Column(name = "COM_REG_SPACE1")
  private String comRegSpace1;

  @Column(name = "COM_REG_FLOOR")
  private String comRegFloor;

  @Column(name = "COM_REG_SPACE2")
  private String comRegSpace2;

  @Column(name = "COM_REG_ROOM")
  private String comRegRoom;

  @Column(name = "COM_ISLOANAMT")
  private String comIsloanamt;

  @Column(name = "COM_LOANAMT")
  private String comLoanamt;

  @Column(name = "COM_MONRENT_AMT")
  private String comMonrentAmt;

  @Column(name = "COM_RENT_YEAR")
  private String comRentYear;

  @Column(name = "CREATE_USER")
  private String createUser;

  @Column(name = "CREATE_TIME")
  private java.sql.Timestamp createTime;

  @Column(name = "UPDATE_USER")
  private String updateUser;

  @Column(name = "UPDATE_TIME")
  private java.sql.Timestamp updateTime;

  @Column(name = "NETINCOMEYEAR")
  private String netincomeyear;


  /**
   * 負責人
   */
  @Column(name = "PRINCIPAL_NAME")
  private String principalName;

  /**
   * 負責人id
   */
  @Column(name = "PRINCIPAL_ID")
  private String principalId;
  /**
   * 負責人配偶
   */
  @Column(name = "PRINCIPAL_SPOUSE_NAME")
  private String principalSpouseName;
  /**
   * 負責人配偶id
   */
  @Column(name = "PRINCIPAL_SPOUSE_ID")
  private String principalSpouseId;
  /**
   * 負責人職業
   */
  @Column(name = "PRINCIPAL_PROFESSION")
  private String principalProfession;
  //公司地址
  @Column(name = "COM_ADDRESS")
  private String comAddress;
  //客戶屬性 01：新戶 02：舊戶
  @Transient
  private String comCustomerType;

  public String getComAddress() {
    return comAddress;
  }

  public void setComAddress(String comAddress) {
    this.comAddress = comAddress;
  }

  public String getPrincipalName() {
    return principalName;
  }

  public void setPrincipalName(String principalName) {
    this.principalName = principalName;
  }

  public String getPrincipalId() {
    return principalId;
  }

  public void setPrincipalId(String principalId) {
    this.principalId = principalId;
  }

  public String getPrincipalSpouseName() {
    return principalSpouseName;
  }

  public void setPrincipalSpouseName(String principalSpouseName) {
    this.principalSpouseName = principalSpouseName;
  }

  public String getPrincipalSpouseId() {
    return principalSpouseId;
  }

  public void setPrincipalSpouseId(String principalSpouseId) {
    this.principalSpouseId = principalSpouseId;
  }

  public String getPrincipalProfession() {
    return principalProfession;
  }

  public void setPrincipalProfession(String principalProfession) {
    this.principalProfession = principalProfession;
  }

  public String getCompilationNo() {
    return compilationNo;
  }

  public void setCompilationNo(String compilationNo) {
    this.compilationNo = compilationNo;
  }


  public String getComName() {
    return comName;
  }

  public void setComName(String comName) {
    this.comName = comName;
  }


  public String getComOrganization() {
    return comOrganization;
  }

  public void setComOrganization(String comOrganization) {
    this.comOrganization = comOrganization;
  }

  public String getComOrganizationName() {
    return comOrganizationName;
  }

  public void setComOrganizationName(String comOrganizationName) {
    this.comOrganizationName = comOrganizationName;
  }

  public Date getComEstabdate() {
    return comEstabdate;
  }

  public void setComEstabdate(Date comEstabdate) {
    this.comEstabdate = comEstabdate;
  }


  public String getComScale() {
    return comScale;
  }

  public void setComScale(String comScale) {
    this.comScale = comScale;
  }


  public String getComStaffNum() {
    return comStaffNum;
  }

  public void setComStaffNum(String comStaffNum) {
    this.comStaffNum = comStaffNum;
  }


  public String getComTaxationCode() {
    return comTaxationCode;
  }

  public void setComTaxationCode(String comTaxationCode) {
    this.comTaxationCode = comTaxationCode;
  }


  public String getComIndustry() {
    return comIndustry;
  }

  public void setComIndustry(String comIndustry) {
    this.comIndustry = comIndustry;
  }


  public String getComCreditIndustryCode() {
    return comCreditIndustryCode;
  }

  public void setComCreditIndustryCode(String comCreditIndustryCode) {
    this.comCreditIndustryCode = comCreditIndustryCode;
  }


  public String getComCreditIndustryName() {
    return comCreditIndustryName;
  }

  public void setComCreditIndustryName(String comCreditIndustryName) {
    this.comCreditIndustryName = comCreditIndustryName;
  }


  public String getComObject() {
    return comObject;
  }

  public void setComObject(String comObject) {
    this.comObject = comObject;
  }


  public String getComActualCapital() {
    return comActualCapital;
  }

  public void setComActualCapital(String comActualCapital) {
    this.comActualCapital = comActualCapital;
  }


  public String getComActualCapitalDate() {
    return comActualCapitalDate;
  }

  public void setComActualCapitalDate(String comActualCapitalDate) {
    this.comActualCapitalDate = comActualCapitalDate;
  }


  public String getComPreYearIncome() {
    return comPreYearIncome;
  }

  public void setComPreYearIncome(String comPreYearIncome) {
    this.comPreYearIncome = comPreYearIncome;
  }


  public String getComMainproduct() {
    return comMainproduct;
  }

  public void setComMainproduct(String comMainproduct) {
    this.comMainproduct = comMainproduct;
  }


  public String getComBusinessItem() {
    return comBusinessItem;
  }

  public void setComBusinessItem(String comBusinessItem) {
    this.comBusinessItem = comBusinessItem;
  }


  public String getComRecyearRevenueInterval() {
    return comRecyearRevenueInterval;
  }

  public void setComRecyearRevenueInterval(String comRecyearRevenueInterval) {
    this.comRecyearRevenueInterval = comRecyearRevenueInterval;
  }


  public String getComIsLaborinsurNumber() {
    return comIsLaborinsurNumber;
  }

  public void setComIsLaborinsurNumber(String comIsLaborinsurNumber) {
    this.comIsLaborinsurNumber = comIsLaborinsurNumber;
  }


  public String getComLaborinsurNumber() {
    return comLaborinsurNumber;
  }

  public void setComLaborinsurNumber(String comLaborinsurNumber) {
    this.comLaborinsurNumber = comLaborinsurNumber;
  }


  public String getComRecyearRevenueDate() {
    return comRecyearRevenueDate;
  }

  public void setComRecyearRevenueDate(String comRecyearRevenueDate) {
    this.comRecyearRevenueDate = comRecyearRevenueDate;
  }


  public String getComRecyearRevenue() {
    return comRecyearRevenue;
  }

  public void setComRecyearRevenue(String comRecyearRevenue) {
    this.comRecyearRevenue = comRecyearRevenue;
  }


  public String getComRegPostOfficeCode() {
    return comRegPostOfficeCode;
  }

  public void setComRegPostOfficeCode(String comRegPostOfficeCode) {
    this.comRegPostOfficeCode = comRegPostOfficeCode;
  }


  public String getComRegCityCode() {
    return comRegCityCode;
  }

  public void setComRegCityCode(String comRegCityCode) {
    this.comRegCityCode = comRegCityCode;
  }


  public String getComRegDistrictCode() {
    return comRegDistrictCode;
  }

  public void setComRegDistrictCode(String comRegDistrictCode) {
    this.comRegDistrictCode = comRegDistrictCode;
  }


  public String getComRegStreetCode() {
    return comRegStreetCode;
  }

  public void setComRegStreetCode(String comRegStreetCode) {
    this.comRegStreetCode = comRegStreetCode;
  }


  public String getComRegTunnel() {
    return comRegTunnel;
  }

  public void setComRegTunnel(String comRegTunnel) {
    this.comRegTunnel = comRegTunnel;
  }


  public String getComRegLane() {
    return comRegLane;
  }

  public void setComRegLane(String comRegLane) {
    this.comRegLane = comRegLane;
  }


  public String getComRegAddnumber() {
    return comRegAddnumber;
  }

  public void setComRegAddnumber(String comRegAddnumber) {
    this.comRegAddnumber = comRegAddnumber;
  }


  public String getComRegSpace1() {
    return comRegSpace1;
  }

  public void setComRegSpace1(String comRegSpace1) {
    this.comRegSpace1 = comRegSpace1;
  }


  public String getComRegFloor() {
    return comRegFloor;
  }

  public void setComRegFloor(String comRegFloor) {
    this.comRegFloor = comRegFloor;
  }


  public String getComRegSpace2() {
    return comRegSpace2;
  }

  public void setComRegSpace2(String comRegSpace2) {
    this.comRegSpace2 = comRegSpace2;
  }


  public String getComRegRoom() {
    return comRegRoom;
  }

  public void setComRegRoom(String comRegRoom) {
    this.comRegRoom = comRegRoom;
  }


  public String getComIsloanamt() {
    return comIsloanamt;
  }

  public void setComIsloanamt(String comIsloanamt) {
    this.comIsloanamt = comIsloanamt;
  }


  public String getComLoanamt() {
    return comLoanamt;
  }

  public void setComLoanamt(String comLoanamt) {
    this.comLoanamt = comLoanamt;
  }


  public String getComMonrentAmt() {
    return comMonrentAmt;
  }

  public void setComMonrentAmt(String comMonrentAmt) {
    this.comMonrentAmt = comMonrentAmt;
  }


  public String getComRentYear() {
    return comRentYear;
  }

  public void setComRentYear(String comRentYear) {
    this.comRentYear = comRentYear;
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

  public String getNetincomeyear() {
    return netincomeyear;
  }

  public void setNetincomeyear(String netincomeyear) {
    this.netincomeyear = netincomeyear;
  }

  public String getComCustomerType() {
    return comCustomerType;
  }

  public void setComCustomerType(String comCustomerType) {
    this.comCustomerType = comCustomerType;
  }
}
