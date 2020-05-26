package com.fb.goldencudgel.auditDecisionSystem.domain.letterConsent;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 联征同意书
 * @author z
 *
 */
@Entity
@Table(name = "FB_LETTER_CONSENT")
public class FbLetterConsent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="LETTERID")
    private String letterId;
	
	@Column(name ="COMPILATION_NO")
	private String compilationNo;
	
	@Column(name ="CUSTOMER_NAME")
	private String customerName;
	
	@Column(name ="AUDIT_STATUS")
	private String auditStatus;
	
	@Column(name ="ORIGINATOR_USER")
	private String originatorUser;
	
	@Column(name ="APPROVER_USER")
	private String approveUser;
	
	@Column(name ="CREATE_TIME")
	private Date createTime;

	@Column(name = "SUMBIT_TYPE")
	private String submitType;
	
	
	public String getLetterId() {
    return letterId;
    }

    public void setLetterId(String letterId) {
    this.letterId = letterId;
    }

    public String getCompilationNo() {
		return compilationNo;
	}

	public void setCompilationNo(String compilationNo) {
		this.compilationNo = compilationNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getOriginatorUser() {
		return originatorUser;
	}

	public void setOriginatorUser(String originatorUser) {
		this.originatorUser = originatorUser;
	}

	public String getApproveUser() {
		return approveUser;
	}

	public void setApproveUser(String approveUser) {
		this.approveUser = approveUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSubmitType() {
		return submitType;
	}

	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}

}
