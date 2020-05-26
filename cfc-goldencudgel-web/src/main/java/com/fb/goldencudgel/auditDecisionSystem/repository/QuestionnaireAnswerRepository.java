package com.fb.goldencudgel.auditDecisionSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireAnswer;

import javax.transaction.Transactional;

@Repository
public interface QuestionnaireAnswerRepository  extends JpaRepository<FbQuestionnaireAnswer, String> {

  @Query("from FbQuestionnaireAnswer where question.id=:detailId")
  List<FbQuestionnaireAnswer> findByQuestionId(@Param("detailId")String detailId);

  @Modifying
  @Transactional
  @Query("update FbQuestionnaireAnswer set question.id =:defaultId where question.id=:detailId")
  void updateQuestionId(@Param("defaultId")String defaultId,@Param("detailId")String detailId);

}
