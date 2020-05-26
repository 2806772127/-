package com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.FbQuestionRecord;
import org.hibernate.annotations.GenericGenerator;

/**
  * 
  * FB_QUESTIONNAIRE_DETAIL 实体类
  *
  * @date 2018-12-17 16:43:36,311 
  * @author zou
  */ 
@Entity
@Table(name = "FB_QUESTIONNAIRE_DETAIL")
public class FbQuestionnaireDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String id;
	@Column(name = "QUESTION_TYPE")
	private String questionType;
	@Column(name = "NAME")
	private String name;
	@Column(name = "RULE_ITEM")
	private String ruleItem;
	@Column(name = "RULE_ITEM_SIGNIFICANCE")
	private String ruleItemSignificance;
	@Column(name = "APPLY_ITEM")
	private String applyItem;
	@Column(name = "IS_REQUIRED")
	private String isRequired;
	@Column(name = "SORT_NO")
	private String sortNo;
	@Column(name = "QUESTION_LEVEL")
	private String questionLevel;  //问题级别 1-主问题 2-副问题
	@Column(name = "FATHER_QUERTION")
	private String fatherQuestion;  //所属主问题
	@Column(name = "DEFAULT_ID")
	private String defaultId;  //默认题目ID
	@Transient
	private String answer;
	@Transient
	private String fatherQuesId;//所属主问题ID
	@Transient
	private FbQuestionRecord questionRecord; // 答案记录
	@ManyToOne
    @JoinColumn(name = "QUESTIONNAIRE_ID")
	private FbQuestionnaire questionnaire;
	@OneToMany(mappedBy = "question")
	List<FbQuestionnaireAnswer> answers;
	
	@Column(name = "DEFUALT_ANSWER")
	private String defualtAnswer;//默認答案
	
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
	
	public String getQuestionType() {
		return questionType;
	}
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}
	public String getRuleItem() {
		return ruleItem;
	}
	public void setRuleItem(String ruleItem) {
		this.ruleItem = ruleItem;
	}
	public String getRuleItemSignificance() {
		return ruleItemSignificance;
	}
	public void setRuleItemSignificance(String ruleItemSignificance) {
		this.ruleItemSignificance = ruleItemSignificance;
	}
	public String getApplyItem() {
		return applyItem;
	}
	public void setApplyItem(String applyItem) {
		this.applyItem = applyItem;
	}
	public void setSortNo(String sortNo){
		this.sortNo = sortNo;
	}
	public String getSortNo(){
		return sortNo;
	}
	public FbQuestionnaire getQuestionnaire() {
		return questionnaire;
	}
	public void setQuestionnaire(FbQuestionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
	public List<FbQuestionnaireAnswer> getAnswers() {
		return answers;
	}
	public void setAnswers(List<FbQuestionnaireAnswer> answers) {
		this.answers = answers;
	}
	public String getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
	public String getQuestionLevel() {
		return questionLevel;
	}
	public void setQuestionLevel(String questionLevel) {
		this.questionLevel = questionLevel;
	}
	public String getFatherQuestion() {
		return fatherQuestion;
	}
	public void setFatherQuestion(String fatherQuestion) {
		this.fatherQuestion = fatherQuestion;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getFatherQuesId(){ return  fatherQuesId; }
	public void setFatherQuesId(String fatherQuesId){ this.fatherQuesId = fatherQuesId; }
    public String getDefualtAnswer() {
      return defualtAnswer;
    }
    public void setDefualtAnswer(String defualtAnswer) {
      this.defualtAnswer = defualtAnswer;
    }
	public FbQuestionRecord getQuestionRecord() {
		return questionRecord;
	}
	public void setQuestionRecord(FbQuestionRecord questionRecord) {
		this.questionRecord = questionRecord;
	}
	public String getDefaultId() {
		return defaultId;
	}
	public void setDefaultId(String defaultId) {
		this.defaultId = defaultId;
	}
}
