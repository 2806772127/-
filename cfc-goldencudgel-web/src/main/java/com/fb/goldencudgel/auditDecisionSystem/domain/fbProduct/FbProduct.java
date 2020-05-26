package com.fb.goldencudgel.auditDecisionSystem.domain.fbProduct;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="FB_PRODUCT")
public class FbProduct implements Serializable {

    @Id
    @Column(name = "PROD_ID")
    private String prodId;

    @Column(name = "PROD_NAME")
    private String prodName;

    @Column(name = "MEASURE_WORD_CONFIG")
    private String measureWordConfig;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "CREATE_USER_NAME")
    private String createUSerName;

    @Column(name = "UPDATE_USER")
    private String updateUser;

    @Column(name = "UPDATE_USER_NAME")
    private String updateUserName;

    @Column(name = "CREATE_TIME")
    private Timestamp createTime;

    @Column(name = "UPDATE_TIME")
    private Timestamp updateTime;
    
    @Column(name = "MISSION_SCHEDULE")
    private String missionSchedule;

    @Column(name = "VISIT_NOTE")
    private String visitNote;

    @Column(name = "LETTER_CONSENT")
    private String letterConsent;
    
    @Column(name = "MEASURE_WORD")
    private String measureWord;

    @Column(name = "APPLY_INCOM")
    private String applyIncom;

    @Column(name = "CREDIT_REPORT")
    private String creditReport;
    
    @Column(name = "ESTIMATED_LAUNCH_TIME")
    private Timestamp estimatedLaunchTime;

    @Column(name = "QUESTIONNAIRE_TYPE")
    private String questionnaireType;

    @Column(name = "AUDIT_STATE")
    private String auditState;
    
    @Column(name = "SET_ITEM")
    private String setItem;
    
    @Column(name = "PRODUCT_VERSION")
    private String productVersion;
    
    @Column(name = "AUDIT_REMARK")
    private String auditRemark;
    
    @Column(name = "CHECK_RESULT")
    private String checkResult;
    
    @Column(name = "ONLINE_STATE")
    private String onlineState;
    
    

	public String getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(String onlineState) {
		this.onlineState = onlineState;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}


	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public String getSetItem() {
		return setItem;
	}

	public void setSetItem(String setItem) {
		this.setItem = setItem;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getMeasureWordConfig() {
		return measureWordConfig;
	}

	public void setMeasureWordConfig(String measureWordConfig) {
		this.measureWordConfig = measureWordConfig;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateUSerName() {
		return createUSerName;
	}

	public void setCreateUSerName(String createUSerName) {
		this.createUSerName = createUSerName;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getMissionSchedule() {
		return missionSchedule;
	}

	public void setMissionSchedule(String missionSchedule) {
		this.missionSchedule = missionSchedule;
	}

	public String getVisitNote() {
		return visitNote;
	}

	public void setVisitNote(String visitNote) {
		this.visitNote = visitNote;
	}

	public String getLetterConsent() {
		return letterConsent;
	}

	public void setLetterConsent(String letterConsent) {
		this.letterConsent = letterConsent;
	}

	public String getMeasureWord() {
		return measureWord;
	}

	public void setMeasureWord(String measureWord) {
		this.measureWord = measureWord;
	}

	public String getApplyIncom() {
		return applyIncom;
	}

	public void setApplyIncom(String applyIncom) {
		this.applyIncom = applyIncom;
	}

	public String getCreditReport() {
		return creditReport;
	}

	public void setCreditReport(String creditReport) {
		this.creditReport = creditReport;
	}

	public Timestamp getEstimatedLaunchTime() {
		return estimatedLaunchTime;
	}

	public void setEstimatedLaunchTime(Timestamp estimatedLaunchTime) {
		this.estimatedLaunchTime = estimatedLaunchTime;
	}

	public String getQuestionnaireType() {
		return questionnaireType;
	}

	public void setQuestionnaireType(String questionnaireType) {
		this.questionnaireType = questionnaireType;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}
	
	
    
}

    