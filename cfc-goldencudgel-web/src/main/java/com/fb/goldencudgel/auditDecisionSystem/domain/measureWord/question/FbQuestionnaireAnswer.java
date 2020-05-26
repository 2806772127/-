package com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question;

import java.io.Serializable;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
  * 
  * FB_QUESTIONNAIRE_ANSWER 实体类
  *
  * @date 2018-12-17 16:43:36,413 
  * @author zou
  */ 
@Entity
@Table(name = "FB_QUESTIONNAIRE_ANSWER")
public class FbQuestionnaireAnswer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String id;
	
	@Column(name = "ANSWER")
	private String answer;
	@Column(name = "NEXT_QUESTION")
	private String nextQuestion;
	@Transient
	private String nextQuesId;
	
	@ManyToOne
    @JoinColumn(name = "QUESTIONNAIRE_DETAIL_ID")
	private FbQuestionnaireDetail question;
	
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	public void setAnswer(String answer){
		this.answer = answer;
	}
	public String getAnswer(){
		return answer;
	}
	public void setNextQuestion(String nextQuestion){
		this.nextQuestion = nextQuestion;
	}
	public String getNextQuestion(){
		return nextQuestion;
	}
	public FbQuestionnaireDetail getQuestion() {
		return question;
	}
	public void setQuestion(FbQuestionnaireDetail question) {
		this.question = question;
	}
	public String getNextQuesId(){ return nextQuesId; }
	public void setNextQuesId(String nextQuesId){ this.nextQuesId = nextQuesId; }
	
}
