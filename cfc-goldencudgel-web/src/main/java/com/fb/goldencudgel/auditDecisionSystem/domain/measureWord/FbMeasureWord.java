package com.fb.goldencudgel.auditDecisionSystem.domain.measureWord;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
  * 
  * FB_MEASURE_WORD 实体类
  *
  * @date 2018-12-17 16:43:36,202 
  * @author zou
  */ 
@Entity
@Table(name = "FB_MEASURE_WORD")
public class FbMeasureWord implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MEASURE_ID", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String measureId;
	@Column(name = "COMPILATION_NO")
	private String compilationNo;
	@Column(name = "TRAND_ID")
	private String trandId;
	@Column(name = "MEASURE_DATE")
	private Date measureDate;
	@Column(name = "CUSTOMER_NAME")
	private String customerName;
	@Column(name = "PROD_CODE")
	private String prodCode;
	@Column(name = "PROD_NAME")
	private String prodName;
	@Column(name = "MEASURE_RESULT")
	private String measureResult;
	@Column(name = "SOLUTION")
	private String solution;
	@Column(name = "SOLUTION_EXPLAIN")
	private String solutionExplain;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CREATE_USER")
	private String createUser;

	@Column(name = "ZJX_FLAG")
	private String zjxFlag;

	@Column(name = "ZJX_STATUS")
	private String zjxStatus;

	@Column(name = "ZJX_RESP_MSG")
	private String zjxRespMsg;

	@Column(name = "ZJX_ID")
	private String zjxId;

	@Column(name = "SOLUTION_URL")
	private String solutionUrl;

	@Column(name = "SEND_TIME")
	private Date sendTime;

	@Column(name = "SEND_END_TIME")
	private Date sendEndTime;

	@Column(name = "REPLY_TIME")
	private Date replyTime;

	@Column(name = "REPLY_END_TIME")
	private Date replyEndTime;

	@Column(name = "COMP_RECENT_AMOUNT")
	private String compRecentAmount;

	@Column(name = "COMP_REC_CREDIT_AMOUNT")
	private String compRecCreditAmount;

	@Column(name = "PRINCIPAL_FINAL_AMOUTN")
	private String principalFinalAmoutn;

	@Column(name = "PRINCIPAL_BORROW_AMOUNT")
	private String principalBorrowAmount;

	@Column(name = "PRINCIPAL_SPOUSE_FINAL_AMOUNT")
	private String principalSpouseFinalAmount;

	@Column(name = "PRINCIPAL_SPOUSE_BORROW_AMOUNT")
	private String principalSpouseBorrowAmount;

	@Column(name = "UPDATA_DATE")
	private Date updataDate;

	@Column(name = "TEMP_FLAG")
	private String tempFlag;

	@Column(name = "QUESTION_TYPE")
	private String questionType;

    @Column(name = "ATTACH_ID")
    private String attachId;

	@Column(name = "SCHEDULER_ID")
	private String schedulerId;

	public String getSchedulerId() {
		return schedulerId;
	}

	public void setSchedulerId(String schedulerId) {
		this.schedulerId = schedulerId;
	}



	@Transient
	private List<FbQuestionRecord> records  = new ArrayList<>();

	public List<FbQuestionRecord> getRecords() {
		return records;
	}

	public void setRecords(List<FbQuestionRecord> records) {
		this.records = records;
	}

	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}
	public void setCompilationNo(String compilationNo){
		this.compilationNo = compilationNo;
	}
	public String getCompilationNo(){
		return compilationNo;
	}
	public void setTrandId(String trandId){
		this.trandId = trandId;
	}
	public String getTrandId(){
		return trandId;
	}
	public void setMeasureDate(Date measureDate){
		this.measureDate = measureDate;
	}
	public Date getMeasureDate(){
		return measureDate;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getProdName() {
		return prodName;
	}
	public void setProdName(String prodName) {
		this.prodName = prodName;
	}
	public String getMeasureResult() {
		return measureResult;
	}
	public void setMeasureResult(String measureResult) {
		this.measureResult = measureResult;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	public String getSolutionExplain() {
		return solutionExplain;
	}
	public void setSolutionExplain(String solutionExplain) {
		this.solutionExplain = solutionExplain;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getZjxFlag() {
		return zjxFlag;
	}

	public void setZjxFlag(String zjxFlag) {
		this.zjxFlag = zjxFlag;
	}

	public String getZjxStatus() {
		return zjxStatus;
	}

	public void setZjxStatus(String zjxStatus) {
		this.zjxStatus = zjxStatus;
	}

	public String getZjxRespMsg() {
		return zjxRespMsg;
	}

	public void setZjxRespMsg(String zjxRespMsg) {
		this.zjxRespMsg = zjxRespMsg;
	}

	public String getZjxId() {
		return zjxId;
	}

	public void setZjxId(String zjxId) {
		this.zjxId = zjxId;
	}

	public String getSolutionUrl() {
		return solutionUrl;
	}

	public void setSolutionUrl(String solutionUrl) {
		this.solutionUrl = solutionUrl;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getSendEndTime() {
		return sendEndTime;
	}

	public void setSendEndTime(Date sendEndTime) {
		this.sendEndTime = sendEndTime;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public Date getReplyEndTime() {
		return replyEndTime;
	}

	public void setReplyEndTime(Date replyEndTime) {
		this.replyEndTime = replyEndTime;
	}

	public String getCompRecentAmount() {
		return compRecentAmount;
	}

	public void setCompRecentAmount(String compRecentAmount) {
		this.compRecentAmount = compRecentAmount;
	}

	public String getCompRecCreditAmount() {
		return compRecCreditAmount;
	}

	public void setCompRecCreditAmount(String compRecCreditAmount) {
		this.compRecCreditAmount = compRecCreditAmount;
	}

	public String getPrincipalFinalAmoutn() {
		return principalFinalAmoutn;
	}

	public void setPrincipalFinalAmoutn(String principalFinalAmoutn) {
		this.principalFinalAmoutn = principalFinalAmoutn;
	}

	public String getPrincipalBorrowAmount() {
		return principalBorrowAmount;
	}

	public void setPrincipalBorrowAmount(String principalBorrowAmount) {
		this.principalBorrowAmount = principalBorrowAmount;
	}

	public String getPrincipalSpouseFinalAmount() {
		return principalSpouseFinalAmount;
	}

	public void setPrincipalSpouseFinalAmount(String principalSpouseFinalAmount) {
		this.principalSpouseFinalAmount = principalSpouseFinalAmount;
	}

	public Date getUpdataDate() {
		return updataDate;
	}

	public void setUpdataDate(Date updataDate) {
		this.updataDate = updataDate;
	}

	public String getPrincipalSpouseBorrowAmount() {
		return principalSpouseBorrowAmount;
	}

	public void setPrincipalSpouseBorrowAmount(String principalSpouseBorrowAmount) {
		this.principalSpouseBorrowAmount = principalSpouseBorrowAmount;
	}

	public String getTempFlag() {
		return tempFlag;
	}

	public void setTempFlag(String tempFlag) {
		this.tempFlag = tempFlag;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }
}
