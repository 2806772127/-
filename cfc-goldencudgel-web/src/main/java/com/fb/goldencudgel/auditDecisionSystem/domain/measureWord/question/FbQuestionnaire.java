package com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
  * 
  * 問卷題庫 實體類
  *
  * @date 2018-12-17 16:43:36,086 
  * @author zou
  */ 
@Entity
@Table(name = "FB_QUESTIONNAIRE")
public class FbQuestionnaire implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "CREATE_DATE")
	private Date createDate;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "QUESTIONNAIRE_TYPE")
	private String questionniareType;
	@Column(name = "IS_ENABLE")
	private String isEnable;
	@Column(name = "PROD_CODE")
	private String prodCode;

	/** 問卷所屬 01：測字館 02：徵信實訪 **/
	@Column(name="QUESTION_TYPE")
	private String introduceType;

	@Column(name = "CREATE_USER")
	private String createUser;
	@Column(name = "CREATE_USER_CODE")
	private String createUserCode;
	@Column(name = "AUDIT_USER_CODE")
	private String auditUserCode;
	@Column(name = "AUDIT_USER")
	private String auditUser;
	@Column(name = "AUDIT_STATUS")
	private String auditStatus;
	@Column(name = "UPDATE_REMARKS")
	private String updateRemark;
	@Column(name = "COPY_ORIGINAL_QUESTIONNAIRE_ID")
	private String copyOriginalQuestionnaireId;
	@Column(name = "AUDIT_RESULT")
	private String auditResult;
	@Column(name = "AUDIT_REMARK")
	private String auditRemark;
	@Column(name = "DELETE_FLAG")
	private String deleteFlag;
	@Column(name = "ADD_DEL_UPDATE_FLAG")
	private String addDelUpdateFlag;
	@Column(name = "AUDIT_DATE")
	private String auditDate;
	@Column(name = "COMMIT_DATE")
	private String commitDate;
	@Column(name = "AUDIT_CONFIG_USER")
	private String auditConfigUser;

	@OneToMany(mappedBy = "questionnaire")
	List<FbQuestionnaireDetail> details;
	
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	public Date getCreateDate(){
		return createDate;
	}
	public void setDescription(String description){
		this.description = description;
	}
	public String getDescription(){
		return description;
	}
	public String getQuestionniareType() {
		return questionniareType;
	}
	public void setQuestionniareType(String questionniareType) {
		this.questionniareType = questionniareType;
	}
	public String getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public List<FbQuestionnaireDetail> getDetails() {
		return details;
	}
	public void setDetails(List<FbQuestionnaireDetail> details) {
		this.details = details;
	}
	public String getIntroduceType() {
		return introduceType;
	}
	public void setIntroduceType(String questionType) {
		this.introduceType = questionType;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getCopyOriginalQuestionnaireId() {
		return copyOriginalQuestionnaireId;
	}
	public void setCopyOriginalQuestionnaireId(String copyOriginalQuestionnaireId) {
		this.copyOriginalQuestionnaireId = copyOriginalQuestionnaireId;
	}
	public String getUpdateRemark() {
		return updateRemark;
	}
	public void setUpdateRemark(String updateRemark) {
		this.updateRemark = updateRemark;
	}
	public String getAuditResult() {
		return auditResult;
	}
	public void setAuditResult(String auditResult) {
		this.auditResult = auditResult;
	}
	public String getAuditRemark() {
		return auditRemark;
	}
	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getAddDelUpdateFlag() {
		return addDelUpdateFlag;
	}
	public void setAddDelUpdateFlag(String addDelUpdateFlag) {
		this.addDelUpdateFlag = addDelUpdateFlag;
	}
	public String getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	public String getCreateUserCode() {
		return createUserCode;
	}
	public void setCreateUserCode(String createUserCode) {
		this.createUserCode = createUserCode;
	}
	public String getAuditUserCode() {
		return auditUserCode;
	}
	public void setAuditUserCode(String auditUserCode) {
		this.auditUserCode = auditUserCode;
	}
	public String getAuditUser() {
		return auditUser;
	}
	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
	public String getCommitDate() {
		return commitDate;
	}
	public void setCommitDate(String commitDate) {
		this.commitDate = commitDate;
	}
	public String getAuditConfigUser() {
		return auditConfigUser;
	}
	public void setAuditConfigUser(String auditConfigUser) {
		this.auditConfigUser = auditConfigUser;
	}
	
	
    
}
