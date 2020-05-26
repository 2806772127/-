package com.fb.goldencudgel.auditDecisionSystem.domain.visit;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 拜访笔记详情
 * 
 * @author David
 *
 */
@Entity
@Table(name = "FB_VISIT_COMPANY_DETAIL")
public class FbVisitCompanyDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 拜访笔记ID **/
    @Id
    @Column(name = "VISIT_ID")
    private String visitId;
    /** 授信戶統編 **/
    @Column(name = "COMPILATION_NO")
    private String compilationNo;
    /** 授信戶名稱 **/
    @Column(name = "COMPILATION_NAME")
    private String compilationName;
    /** 流水号 **/
    @Column(name = "TRAND_ID")
    private String trandId;
    /** 負債(NT$) **/
    @Column(name = "INCUR_DEBTS_AMOUNTS")
    private BigDecimal incurDebtsAmounts;
    /** 有無不動產 **/
    @Column(name = "HAS_HOURSES_FLAG")
    private String hasHoursesFlag;
    /** 想要產品 **/
    @Column(name = "APPLY_PROD_CODE")
    private String applyProdCode;
    /** 想要產品名称 **/
    @Column(name = "APPLY_PROD_NAME")
    private String applyProdName;
    /** 收款方式 1支票 2現金 3 轉賬 **/
    @Column(name = "RECYCLY_AMOUNT_TYPE")
    private String recyclyAmountType;
    /** 拜访备注 **/
    @Column(name = "VISIT_DESC")
    private String visitDesc;
    /** 拜訪結果 1：同意申请 ：客户婉拒 3：改期拜访 **/
    @Column(name = "VISIT_RESULT_CODE")
    private String visitResultCode;
    /** 打卡历史ID **/
    @Column(name = "PUNCH_CARD_RECODE_ID")
    private String punchCardRecodeId;
    /** 預約下次拜訪時間 **/
    @Column(name = "APPOINTMENT_NEXT_DATE")
    private java.sql.Timestamp appointmentNextDate;
    /** **/
    @Column(name = "CREATE_TIME")
    private java.sql.Timestamp createTime;
    /** **/
    @Column(name = "CREATE_USER")
    private String createUser;
    /** **/
    @Column(name = "UPDATE_TIME")
    private java.sql.Timestamp updateTime;
    /** **/
    @Column(name = "UPDATE_USER")
    private String updateUser;
    /** 公司營業場所狀況 **/
    @Column(name = "BUSINESS_PLACE_TYPE")
    private String businessPlaceType;
    /** 公司目前在銀行週轉借款餘額 **/
    @Column(name = "BANK_LOAN_BALANCE")
    private BigDecimal bankLoanBalance;
    /** 負責人學歷 **/
    @Column(name = "VISIT_EDU")
    private String visitEdu;
    /** 公司登記負責人與實際負責人關係 **/
    @Column(name = "REGIETER_REAL_RELATION")
    private String regieterRealRelation;
    /** 保證人關係 **/
    @Column(name = "ASSURER_RELATION")
    private String assurerRelation;
    /** 公司+負責人+負責人配偶近三個月存款績數 **/
    @Column(name = "THREE_MONTHS_AMOUNTS")
    private BigDecimal threeMonthsAmounts;
    /** 行业别 */
    @Column(name = "COM_INDUSTRY")
    private String comIndustry;
    /** 近一年营收（NT$） */
    @Column(name = "COM_RECYEAR_REVENUE")
    private String comRecyearRevenue;
    /** 婚姻状态 */
    @Column(name = "APPOINTMENT_MARY_STATUS")
    private String appointmentStatus;
    /** 员工人数 */
    @Column(name = "COM_STAFF_NUM")
    private String comStaffNum;
    /** 收款方式名称 */
    @Column(name = "RECYCLY_AMOUNT_TYPE_NAME")
    private String recyclyAmountTypeName;
    /**申请产品*/
    @Column(name = "APPLY_PRODUCT")
    private String  applyProduct;

    /** 拜访笔记提交状态 */
    @Column(name = "CHECK_SUBMIT_STATU")
    private String checkSubmitStatu;

    public String getApplyProduct() {
	return applyProduct;
  }

    public void setApplyProduct(String applyProduct) {
	this.applyProduct = applyProduct;
  }

    public String getComIndustry() {
        return comIndustry;
    }

    public void setComIndustry(String comIndustry) {
        this.comIndustry = comIndustry;
    }

    public String getComRecyearRevenue() {
        return comRecyearRevenue;
    }

    public void setComRecyearRevenue(String comRecyearRevenue) {
        this.comRecyearRevenue = comRecyearRevenue;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getComStaffNum() {
        return comStaffNum;
    }

    public void setComStaffNum(String comStaffNum) {
        this.comStaffNum = comStaffNum;
    }

    public String getRecyclyAmountTypeName() {
        return recyclyAmountTypeName;
    }

    public void setRecyclyAmountTypeName(String recyclyAmountTypeName) {
        this.recyclyAmountTypeName = recyclyAmountTypeName;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getCompilationNo() {
        return compilationNo;
    }

    public void setCompilationNo(String compilationNo) {
        this.compilationNo = compilationNo;
    }

    public String getCompilationName() {
        return compilationName;
    }

    public void setCompilationName(String compilationName) {
        this.compilationName = compilationName;
    }

    public String getTrandId() {
        return trandId;
    }

    public void setTrandId(String trandId) {
        this.trandId = trandId;
    }

    public BigDecimal getIncurDebtsAmounts() {
        return incurDebtsAmounts;
    }

    public void setIncurDebtsAmounts(BigDecimal incurDebtsAmounts) {
        this.incurDebtsAmounts = incurDebtsAmounts;
    }

    public String getHasHoursesFlag() {
        return hasHoursesFlag;
    }

    public void setHasHoursesFlag(String hasHoursesFlag) {
        this.hasHoursesFlag = hasHoursesFlag;
    }

    public String getApplyProdCode() {
        return applyProdCode;
    }

    public void setApplyProdCode(String applyProdCode) {
        this.applyProdCode = applyProdCode;
    }

    public String getApplyProdName() {
        return applyProdName;
    }

    public void setApplyProdName(String applyProdName) {
        this.applyProdName = applyProdName;
    }

    public String getRecyclyAmountType() {
        return recyclyAmountType;
    }

    public void setRecyclyAmountType(String recyclyAmountType) {
        this.recyclyAmountType = recyclyAmountType;
    }

    public String getVisitDesc() {
        return visitDesc;
    }

    public void setVisitDesc(String visitDesc) {
        this.visitDesc = visitDesc;
    }

    public String getVisitResultCode() {
        return visitResultCode;
    }

    public void setVisitResultCode(String visitResultCode) {
        this.visitResultCode = visitResultCode;
    }

    public String getPunchCardRecodeId() {
        return punchCardRecodeId;
    }

    public void setPunchCardRecodeId(String punchCardRecodeId) {
        this.punchCardRecodeId = punchCardRecodeId;
    }

    public java.sql.Timestamp getAppointmentNextDate() {
        return appointmentNextDate;
    }

    public void setAppointmentNextDate(java.sql.Timestamp appointmentNextDate) {
        this.appointmentNextDate = appointmentNextDate;
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

    public String getBusinessPlaceType() {
        return businessPlaceType;
    }

    public void setBusinessPlaceType(String businessPlaceType) {
        this.businessPlaceType = businessPlaceType;
    }

    public BigDecimal getBankLoanBalance() {
        return bankLoanBalance;
    }

    public void setBankLoanBalance(BigDecimal bankLoanBalance) {
        this.bankLoanBalance = bankLoanBalance;
    }

    public String getVisitEdu() {
        return visitEdu;
    }

    public void setVisitEdu(String visitEdu) {
        this.visitEdu = visitEdu;
    }

    public String getRegieterRealRelation() {
        return regieterRealRelation;
    }

    public void setRegieterRealRelation(String regieterRealRelation) {
        this.regieterRealRelation = regieterRealRelation;
    }

    public String getAssurerRelation() {
        return assurerRelation;
    }

    public void setAssurerRelation(String assurerRelation) {
        this.assurerRelation = assurerRelation;
    }

    public BigDecimal getThreeMonthsAmounts() {
        return threeMonthsAmounts;
    }

    public void setThreeMonthsAmounts(BigDecimal threeMonthsAmounts) {
        this.threeMonthsAmounts = threeMonthsAmounts;
    }

    public String getCheckSubmitStatu() {
        return checkSubmitStatu;
    }

    public void setCheckSubmitStatu(String checkSubmitStatu) {
        this.checkSubmitStatu = checkSubmitStatu;
    }

}
