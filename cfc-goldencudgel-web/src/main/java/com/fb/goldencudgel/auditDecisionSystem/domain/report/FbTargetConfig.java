package com.fb.goldencudgel.auditDecisionSystem.domain.report;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 達成率報表 實體類
 * @author David Ma
 *
 * */
@Entity
@Table(name = "FB_TARGET_CONFIG")
public class FbTargetConfig implements Serializable {
  private static final long serialVersionUID = 1L;
  /** 主键**/
  @Id
  @Column(name = "TARGET_ID", unique = true, nullable = false, length = 36)
  @GeneratedValue(generator = "idGenerator")
  @GenericGenerator(name = "idGenerator", strategy = "uuid")
  private String targetId;
  /** 業務員**/
  @Column(name = "TARGET_USER")
  private String targetUser;
  /** 指標**/
  @Column(name = "TARGET_TYPE")
  private String targetType;
  /** 目標值**/
  @Column(name = "TARGET_VALUE")
  private BigDecimal targetValue;
  /** 週期YYYYMM 目标年月**/
  @Column(name = "TARGET_CYCLE")
  private String targetCycle;
  /** 操作日期**/
  @Column(name = "OPERATION_TIME")
  private Date operationTime;
  /** 创建时间**/
  @Column(name = "CREATE_TIME")
  private Date createTime;
  /** 创建人**/
  @Column(name = "CREATE_USER")
  private String createUser;
  /** 业务员USER_CODE**/
  @Column(name = "TARGET_USER_CODE")
  private String targetUserCode;
  /**企貸進件目標值**/
  @Column(name = "COM_LOAN_ACOUNT")
  private BigDecimal comLoanAcount;
  /**信貸進件目標值 **/
  @Column(name = "CREDIT_LOAN_ACOUNT")
  private BigDecimal creditLoanAcount;
  /** 房貸進件目標值**/
  @Column(name = "HOUSE_LOAN_ACOUNT")
  private BigDecimal houseLoanAcount;
  /** 企貸拨款目標值**/
  @Column(name = "COM_APPROVE_AMT")
  private BigDecimal comApproveAmt;
  /** 信貸拨款目標值**/
  @Column(name = "CREDIT_APPROVE_AMT")
  private BigDecimal creditApproveAmt;
  /** 房貸拨款目標值**/
  @Column(name = "HOUSE_APPROVE_AMT")
  private BigDecimal houseApproveAmt;


  public String getTargetId() { return targetId; }
  public void setTargetId(String targetId) { this.targetId = targetId; }
  public String getTargetUser() { return targetUser; }
  public void setTargetUser(String targetUser) { this.targetUser = targetUser; }
  public String getTargetType() { return targetType; }
  public void setTargetType(String targetType) { this.targetType = targetType; }
  public BigDecimal getTargetValue() { return targetValue; }
  public void setTargetValue(BigDecimal targetValue) { this.targetValue = targetValue; }
  public String getTargetCycle() { return targetCycle; }
  public void setTargetCycle(String targetCycle) { this.targetCycle = targetCycle; }
  public Date getOperationTime() { return operationTime; }
  public void setOperationTime(Date operationTime) { this.operationTime = operationTime; }
  public Date getCreateTime() { return createTime; }
  public void setCreateTime(Date createTime) { this.createTime = createTime; }
  public String getCreateUser() { return createUser; }
  public void setCreateUser(String createUser) { this.createUser = createUser; }
  public String getTargetUserCode() { return targetUserCode; }
  public void setTargetUserCode(String targetUserCode) { this.targetUserCode = targetUserCode; }
  public BigDecimal getComLoanAcount() { return comLoanAcount; }
  public void setComLoanAcount(BigDecimal comLoanAcount) { this.comLoanAcount = comLoanAcount; }
  public BigDecimal getCreditLoanAcount() { return creditLoanAcount; }
  public void setCreditLoanAcount(BigDecimal creditLoanAcount) { this.creditLoanAcount = creditLoanAcount; }
  public BigDecimal getHouseLoanAcount() { return houseLoanAcount; }
  public void setHouseLoanAcount(BigDecimal houseLoanAcount) { this.houseLoanAcount = houseLoanAcount; }
  public BigDecimal getComApproveAmt() { return comApproveAmt; }
  public void setComApproveAmt(BigDecimal comApproveAmt) { this.comApproveAmt = comApproveAmt; }
  public BigDecimal getCreditApproveAmt() { return creditApproveAmt; }
  public void setCreditApproveAmt(BigDecimal creditApproveAmt) { this.creditApproveAmt = creditApproveAmt; }
  public BigDecimal getHouseApproveAmt() { return houseApproveAmt; }
  public void setHouseApproveAmt(BigDecimal houseApproveAmt) { this.houseApproveAmt = houseApproveAmt; }

}
