package com.fb.goldencudgel.auditDecisionSystem.domain.measureWord;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
  *
  * FB_QUESTIONRECORD 实体类
  *
  * @date 2019-05-06 14:00:36
  * @author liren luo
  */
@Entity
@Table(name = "FB_QUESTIONRECORD")
public class FbQuestionRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 36)
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String id;

	@Column(name = "CUS_CODE")
	private String cusCode; // 客户ID

	@Column(name = "QUESTION_TYPE")
	private String questionType;//问卷类型

	@Column(name = "QUESTION_ID")
	private String questionId;// 题目id

	@Column(name = "QUESTUIB_ANSWER")
	private String questuibAnswer; //答案

	@Column(name = "MEASURE_ID")
	private String measureId; // 测字id


	@Column(name = "CREATE_TIME")
	private Date createTime; //

	@Column(name = "TYPE")
	private String type; //题目类型

	@Column(name = "RULE_ITEM")
	private String ruleItem;  // 规则映射栏位

	@Column(name = "APPLY_ITEM")
	private String applyItem; //进件映射栏位

	@Column(name = "QUESTION_NAME")
	private String questionName; //题目内容

	@Column(name = "QUESTION_LEVEL")
	private String questionLevel; //问题级别 1-主问题 2-副问题

	@Column(name = "FATHER_QUERTION")
	private String fatherQuertion; //所属主问题id

	@Column(name = "SAVE_FLAG")
	private String saveFlag;//保存标记 01:暂存 02:保存

	@Transient
	private String republicYear; // 民国历-年

	@Transient
	private String republicMonth; // 民国历-月

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCusCode() {
		return cusCode;
	}

	public void setCusCode(String cusCode) {
		this.cusCode = cusCode;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getQuestuibAnswer() {
		return questuibAnswer;
	}

	public void setQuestuibAnswer(String questuibAnswer) {
		this.questuibAnswer = questuibAnswer;
	}

	public String getMeasureId() {
		return measureId;
	}

	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRuleItem() {
		return ruleItem;
	}

	public void setRuleItem(String ruleItem) {
		this.ruleItem = ruleItem;
	}

	public String getApplyItem() {
		return applyItem;
	}

	public void setApplyItem(String applyItem) {
		this.applyItem = applyItem;
	}

	public String getQuestionName() {
		return questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public String getQuestionLevel() {
		return questionLevel;
	}

	public void setQuestionLevel(String questionLevel) {
		this.questionLevel = questionLevel;
	}

	public String getFatherQuertion() {
		return fatherQuertion;
	}

	public void setFatherQuertion(String fatherQuertion) {
		this.fatherQuertion = fatherQuertion;
	}

	public String getRepublicYear() {
		return republicYear;
	}

	public void setRepublicYear(String republicYear) {
		this.republicYear = republicYear;
	}

	public String getRepublicMonth() {
		return republicMonth;
	}

	public void setRepublicMonth(String republicMonth) {
		this.republicMonth = republicMonth;
	}

	public String getSaveFlag() { return saveFlag; }

	public void setSaveFlag(String saveFlag) { this.saveFlag = saveFlag; }
}
