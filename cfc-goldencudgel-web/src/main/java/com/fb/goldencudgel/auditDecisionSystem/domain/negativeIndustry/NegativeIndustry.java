package com.fb.goldencudgel.auditDecisionSystem.domain.negativeIndustry;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FB_NEGATIVE_INDUSTRIES")
public class NegativeIndustry {

	@Id
	@Column(name = "NEGATIVE_ID")
	private String negativeId;
	@Column(name = "NEGATIVE_TYPE")
	private String negativeType;
	@Column(name = "NEGATIVE_NAME")
	private String negativeName;
	@Column(name = "NEGATIVE_SCORE")
	private String negativeScore;
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getNegativeId() {
		return negativeId;
	}

	public void setNegativeId(String negativeId) {
		this.negativeId = negativeId;
	}

	public String getNegativeType() {
		return negativeType;
	}

	public void setNegativeType(String negativeType) {
		this.negativeType = negativeType;
	}

	public String getNegativeName() {
		return negativeName;
	}

	public void setNegativeName(String negativeName) {
		this.negativeName = negativeName;
	}

	public String getNegativeScore() {
		return negativeScore;
	}

	public void setNegativeScore(String negativeScore) {
		this.negativeScore = negativeScore;
	}
}
