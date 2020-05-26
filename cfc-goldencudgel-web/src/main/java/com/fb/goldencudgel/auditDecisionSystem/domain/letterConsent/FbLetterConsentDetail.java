package com.fb.goldencudgel.auditDecisionSystem.domain.letterConsent;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fb.goldencudgel.auditDecisionSystem.domain.attachment.FbAttachment;

/**
  * 
  * FB_LETTER_CONSENT_DETAIL 联征同意书详情
  *
  * @date 2018-12-26 10:07:54,241 
  * @author zou
  */ 
@Entity
@Table(name = "FB_LETTER_CONSENT_DETAIL")
//@IdClass(FbLetterConsentDetailPK.class)
public class FbLetterConsentDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="LETTER_DETAIL_ID")
    private String letterDetailId;
	
	@Column(name ="LETTERID")
    private String letterId;
	
	@Column(name ="COMPILATION_NO")
	private String compilationNo;

	@Column(name ="LETTER_TYPE")
	private String letterType;
	@Column(name ="ORIGINATOR_USER")
	private String originatorUser;
	@Column(name ="CUSTOMER_ID")
	private String customerId;
	@Column(name ="CUSTOMER_NAME")
	private String customerName;
	
	@Column(name ="APPROVER_USER")
	private String approverUser;
	@Column(name ="APPROVER_DATA")
	private Date approverData;
	@Column(name ="APPROVER_STATUS")
	private String approverStatus;
	@Column(name ="CREATE_TIME")
	private Date createTime;
	@Column(name ="CREATE_USER")
	private String createUser;
	@Column(name ="SHOW_ORDER")
	private String showOrder;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ATTACH_ID",nullable=false)
	private FbAttachment attachment;
	
	public void setCompilationNo(String compilationNo){
		this.compilationNo = compilationNo;
	}
	public String getCompilationNo(){
		return compilationNo;
	}
	public void setLetterType(String letterType){
		this.letterType = letterType;
	}
	public String getLetterType(){
		return letterType;
	}
	public void setOriginatorUser(String originatorUser){
		this.originatorUser = originatorUser;
	}
	public String getOriginatorUser(){
		return originatorUser;
	}
	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}
	public String getCustomerId(){
		return customerId;
	}
	public void setCustomerName(String customerName){
		this.customerName = customerName;
	}
	public String getCustomerName(){
		return customerName;
	}
	public void setApproverUser(String approverUser){
		this.approverUser = approverUser;
	}
	public String getApproverUser(){
		return approverUser;
	}
	public void setApproverData(Date approverData){
		this.approverData = approverData;
	}
	public Date getApproverData(){
		return approverData;
	}
	public void setApproverStatus(String approverStatus){
		this.approverStatus = approverStatus;
	}
	public String getApproverStatus(){
		return approverStatus;
	}
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}
	public String getCreateUser(){
		return createUser;
	}
	public FbAttachment getAttachment() {
		return attachment;
	}
	public void setAttachment(FbAttachment attachment) {
		this.attachment = attachment;
	}
    public String getLetterDetailId() {
    return letterDetailId;
  }
    public void setLetterDetailId(String letterDetailId) {
    this.letterDetailId = letterDetailId;
  }
    public String getLetterId() {
    return letterId;
  }
    public void setLetterId(String letterId) {
    this.letterId = letterId;
  }

	public String getShowOrder() {
		return showOrder;
	}

	public void setShowOrder(String showOrder) {
		this.showOrder = showOrder;
	}
}
