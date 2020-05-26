package com.fb.goldencudgel.auditDecisionSystem.domain.fbProdQuestion;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="FB_PROD_QUESTION")
public class FbProdQuestion implements Serializable {

    @Id
    @Column(name = "PQ_ID")
    private String pqId;

    @Column(name = "VERSION_NUM")
    private String versionNum;

    @Column(name = "QUESTION_ID")
    private String questionId;

    @Column(name = "QUESTION_TYPE")
    private String questionType;

    @Column(name = "PROD_ID")
    private String prodId;

    @Column(name = "LAUNCH_TIME")
    private Timestamp launchTime;
    
    @Column(name = "STORED_FLAG")
    private String storedFlag;
    
    

	public String getStoredFlag() {
		return storedFlag;
	}

	public void setStoredFlag(String storedFlag) {
		this.storedFlag = storedFlag;
	}

	public String getPqId() {
		return pqId;
	}

	public void setPqId(String pqId) {
		this.pqId = pqId;
	}

	public String getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(String versionNum) {
		this.versionNum = versionNum;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public Timestamp getLaunchTime() {
		return launchTime;
	}

	public void setLaunchTime(Timestamp launchTime) {
		this.launchTime = launchTime;
	}

    
	
    
}

    