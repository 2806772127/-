package com.fb.goldencudgel.auditDecisionSystem.domain.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 達成率報表 實體類
 * @author David Ma
 *
 * */
@Entity
@Table(name = "TASK_COMPLISH_STATISTICS")
public class TaskComplishStatistics implements Serializable {
  private static final long serialVersionUID = 1L;
  /** 唯一主键**/
  @Id
  @Column(name = "STATISTICS_ID")
  private String statisticsId;
  /** 统计时间**/
  @Column(name = "STATISTICS_TIME")
  private java.sql.Timestamp statisticsTime;
  /** 所在年份**/
  @Column(name = "STATISTICS_YEAR")
  private String statisticsYear;
  /** 月份（数据按月统计）**/
  @Column(name = "STATISTICS_MONTH")
  private String statisticsMonth;
  /** 區別CODE**/
  @Column(name = "AREA_CODE")
  private String areaCode;
  /** 區別NAME**/
  @Column(name = "AREA_NAME")
  private String areaName;
  /** 組別CODE**/
  @Column(name = "TEAM_CODE")
  private String teamCode;
  /** 組別NAME**/
  @Column(name = "TEAM_NAME")
  private String teamName;
  /**組長 **/
  @Column(name = "TEAM_HEADER")
  private String teamHeader;
  /** 業務員**/
  @Column(name = "ACCOUNT_EXECUTIVER")
  private String accountExecutiver;
  /**企貸進件數 **/
  @Column(name = "COM_LOAN_ACOUNT")
  private BigDecimal comLoanAcount;
  /** 信貸進件數**/
  @Column(name = "CREDIT_LOAN_ACOUNT")
  private BigDecimal creditLoanAcount;
  /** 房貸進件數**/
  @Column(name = "HOUSE_LOAN_ACOUNT")
  private BigDecimal houseLoanAcount;
  /** 目標值**/
  @Column(name = "GOAL_ACOUNT")
  private BigDecimal goalAcount;
  /** 企貸撥款數**/
  @Column(name = "COM_APPRO_ACOUNT")
  private BigDecimal comApproAcount;
  /** 企貸凈增加金額**/
  @Column(name = "COM_INCREASE_AMOUNT")
  private BigDecimal comIncreaseAmount;
  /** 當月目標金額（企貸撥款）**/
  @Column(name = "COM_GOAL_AMOUNT")
  private BigDecimal comGoalAmount;
  /** 信貸撥款數**/
  @Column(name = "CREDIT_APPRO_ACOUNT")
  private BigDecimal creditApproAcount;
  /** 信貸凈增加金額**/
  @Column(name = "CREDIT_INCREASE_AMOUNT")
  private BigDecimal creditIncreaseAmount;
  /**當月目標金額（信貸撥款） **/
  @Column(name = "CREDIT_GOAL_AMOUNT")
  private BigDecimal creditGoalAmount;
  /** 房貸撥款數**/
  @Column(name = "HOUSE_APPRO_ACOUNT")
  private BigDecimal houseApproAcount;
  /** 房貸凈增加金額**/
  @Column(name = "HOUSE_INCREASE_AMOUNT")
  private BigDecimal houseIncreaseAmount;
  /** 當月目標金額（房貸撥款）**/
  @Column(name = "HOUSE_GOAL_AMOUNT")
  private BigDecimal houseGoalAmount;
  /** 創建時間**/
  @Column(name = "CREATE_TIME")
  private java.sql.Timestamp createTime;
  /**創建人 **/
  @Column(name = "CREATE_USER")
  private String createUser;
  /** 更新時間**/
  @Column(name = "UPDATE_TIME")
  private java.sql.Timestamp updateTime;
  /**更新人 **/
  @Column(name = "UPDATE_USER")
  private String updateUser;
  /** 業務員名称**/
  @Column(name = "ACCOUNT_EXECUTIVER_NAME")
  private String accountExecutiverName;
  /** 企貸目標值**/
  @Column(name = "COM_GOAL_ACOUNT")
  private BigDecimal comGoalAcount;
  /** 信貸目標值**/
  @Column(name = "CREDIT_GOAL_ACOUNT")
  private BigDecimal creditGoalAcount;
  /** 房貸目標值**/
  @Column(name = "HOUSE_GOAL_ACOUNT")
  private BigDecimal houseGoalAcount;
  /**企貸撥款目標值 **/
  @Column(name = "COM_APPRO_GOAL_ACOUNT")
  private BigDecimal comApproGoalAcount;
  /** 信貸撥款目標值**/
  @Column(name = "CREDIT_APPRO_GOAL_ACOUNT")
  private BigDecimal creditApproGoalAcount;
  /** 房貸撥款目標值**/
  @Column(name = "HOUSE_APPRO_GOAL_ACOUNT")
  private BigDecimal houseApproGoalAcount;
  /** 企貸进件金额**/
  @Column(name = "COM_APPLY_AMOUNT")
  private BigDecimal comApplyAmount;
  /** 信貸进件金额**/
  @Column(name = "CREDIT_APPLY_AMOUNT")
  private BigDecimal creditApplyAmount;
  /** 房貸进件金额**/
  @Column(name = "HOUSE_APPLY_AMOUNT")
  private BigDecimal houseApplyAmount;


  public String getStatisticsId() {
    return statisticsId;
  }

  public void setStatisticsId(String statisticsId) {
    this.statisticsId = statisticsId;
  }


  public java.sql.Timestamp getStatisticsTime() {
    return statisticsTime;
  }

  public void setStatisticsTime(java.sql.Timestamp statisticsTime) {
    this.statisticsTime = statisticsTime;
  }


  public String getStatisticsYear() {
    return statisticsYear;
  }

  public void setStatisticsYear(String statisticsYear) {
    this.statisticsYear = statisticsYear;
  }


  public String getStatisticsMonth() {
    return statisticsMonth;
  }

  public void setStatisticsMonth(String statisticsMonth) {
    this.statisticsMonth = statisticsMonth;
  }


  public String getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(String areaCode) {
    this.areaCode = areaCode;
  }


  public String getAreaName() {
    return areaName;
  }

  public void setAreaName(String areaName) {
    this.areaName = areaName;
  }


  public String getTeamCode() {
    return teamCode;
  }

  public void setTeamCode(String teamCode) {
    this.teamCode = teamCode;
  }


  public String getTeamName() {
    return teamName;
  }

  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }


  public String getTeamHeader() {
    return teamHeader;
  }

  public void setTeamHeader(String teamHeader) {
    this.teamHeader = teamHeader;
  }


  public String getAccountExecutiver() {
    return accountExecutiver;
  }

  public void setAccountExecutiver(String accountExecutiver) {
    this.accountExecutiver = accountExecutiver;
  }


  public BigDecimal getComLoanAcount() {
    return comLoanAcount;
  }

  public void setComLoanAcount(BigDecimal comLoanAcount) {
    this.comLoanAcount = comLoanAcount;
  }


  public BigDecimal getCreditLoanAcount() {
    return creditLoanAcount;
  }

  public void setCreditLoanAcount(BigDecimal creditLoanAcount) {
    this.creditLoanAcount = creditLoanAcount;
  }


  public BigDecimal getHouseLoanAcount() {
    return houseLoanAcount;
  }

  public void setHouseLoanAcount(BigDecimal houseLoanAcount) {
    this.houseLoanAcount = houseLoanAcount;
  }


  public BigDecimal getGoalAcount() {
    return goalAcount;
  }

  public void setGoalAcount(BigDecimal goalAcount) {
    this.goalAcount = goalAcount;
  }


  public BigDecimal getComApproAcount() {
    return comApproAcount;
  }

  public void setComApproAcount(BigDecimal comApproAcount) {
    this.comApproAcount = comApproAcount;
  }


  public BigDecimal getComIncreaseAmount() {
    return comIncreaseAmount;
  }

  public void setComIncreaseAmount(BigDecimal comIncreaseAmount) {
    this.comIncreaseAmount = comIncreaseAmount;
  }


  public BigDecimal getComGoalAmount() {
    return comGoalAmount;
  }

  public void setComGoalAmount(BigDecimal comGoalAmount) {
    this.comGoalAmount = comGoalAmount;
  }


  public BigDecimal getCreditApproAcount() {
    return creditApproAcount;
  }

  public void setCreditApproAcount(BigDecimal creditApproAcount) {
    this.creditApproAcount = creditApproAcount;
  }


  public BigDecimal getCreditIncreaseAmount() {
    return creditIncreaseAmount;
  }

  public void setCreditIncreaseAmount(BigDecimal creditIncreaseAmount) {
    this.creditIncreaseAmount = creditIncreaseAmount;
  }


  public BigDecimal getCreditGoalAmount() {
    return creditGoalAmount;
  }

  public void setCreditGoalAmount(BigDecimal creditGoalAmount) {
    this.creditGoalAmount = creditGoalAmount;
  }


  public BigDecimal getHouseApproAcount() {
    return houseApproAcount;
  }

  public void setHouseApproAcount(BigDecimal houseApproAcount) {
    this.houseApproAcount = houseApproAcount;
  }


  public BigDecimal getHouseIncreaseAmount() {
    return houseIncreaseAmount;
  }

  public void setHouseIncreaseAmount(BigDecimal houseIncreaseAmount) {
    this.houseIncreaseAmount = houseIncreaseAmount;
  }


  public BigDecimal getHouseGoalAmount() {
    return houseGoalAmount;
  }

  public void setHouseGoalAmount(BigDecimal houseGoalAmount) {
    this.houseGoalAmount = houseGoalAmount;
  }


  public java.sql.Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.sql.Timestamp createTime) {
    this.createTime = createTime;
  }


  public String getCreateUser() {
    return createUser;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }


  public java.sql.Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(java.sql.Timestamp updateTime) {
    this.updateTime = updateTime;
  }


  public String getUpdateUser() {
    return updateUser;
  }

  public void setUpdateUser(String updateUser) {
    this.updateUser = updateUser;
  }


  public String getAccountExecutiverName() {
    return accountExecutiverName;
  }

  public void setAccountExecutiverName(String accountExecutiverName) {
    this.accountExecutiverName = accountExecutiverName;
  }


  public BigDecimal getComGoalAcount() {
    return comGoalAcount;
  }

  public void setComGoalAcount(BigDecimal comGoalAcount) {
    this.comGoalAcount = comGoalAcount;
  }


  public BigDecimal getCreditGoalAcount() {
    return creditGoalAcount;
  }

  public void setCreditGoalAcount(BigDecimal creditGoalAcount) {
    this.creditGoalAcount = creditGoalAcount;
  }


  public BigDecimal getHouseGoalAcount() {
    return houseGoalAcount;
  }

  public void setHouseGoalAcount(BigDecimal houseGoalAcount) {
    this.houseGoalAcount = houseGoalAcount;
  }


  public BigDecimal getComApproGoalAcount() {
    return comApproGoalAcount;
  }

  public void setComApproGoalAcount(BigDecimal comApproGoalAcount) {
    this.comApproGoalAcount = comApproGoalAcount;
  }


  public BigDecimal getCreditApproGoalAcount() {
    return creditApproGoalAcount;
  }

  public void setCreditApproGoalAcount(BigDecimal creditApproGoalAcount) {
    this.creditApproGoalAcount = creditApproGoalAcount;
  }


  public BigDecimal getHouseApproGoalAcount() {
    return houseApproGoalAcount;
  }

  public void setHouseApproGoalAcount(BigDecimal houseApproGoalAcount) {
    this.houseApproGoalAcount = houseApproGoalAcount;
  }


  public BigDecimal getComApplyAmount() {
    return comApplyAmount;
  }

  public void setComApplyAmount(BigDecimal comApplyAmount) {
    this.comApplyAmount = comApplyAmount;
  }


  public BigDecimal getCreditApplyAmount() {
    return creditApplyAmount;
  }

  public void setCreditApplyAmount(BigDecimal creditApplyAmount) {
    this.creditApplyAmount = creditApplyAmount;
  }


  public BigDecimal getHouseApplyAmount() {
    return houseApplyAmount;
  }

  public void setHouseApplyAmount(BigDecimal houseApplyAmount) {
    this.houseApplyAmount = houseApplyAmount;
  }

}
