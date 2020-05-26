package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaireDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface QuestionnaireDetailRepository  extends JpaRepository<FbQuestionnaireDetail, String> {


    @Query("from FbQuestionnaireDetail where id=:detailId")
    FbQuestionnaireDetail findByDetailId(@Param("detailId")String detailId);

    /**
     * 根据问卷id，主问题的sort，获取所有的副问题
     * @param questionnaireId
     * @return
     */
    @Query("from FbQuestionnaireDetail where questionnaire.id=:id ")
    List<FbQuestionnaireDetail> findDetailsByQId(@Param("id")String questionnaireId);

    /**
     * 根据问卷id，主问题的sort，获取所有的副问题
     * @param questionnaireId
     * @return
     */
    @Query("from FbQuestionnaireDetail where questionnaire.id=:id and fatherQuestion=:sortNo ")
    List<FbQuestionnaireDetail> findDetailsByFatherSort(@Param("id")String questionnaireId,@Param("sortNo")String sortNo);

    /**
     * 根据问卷id，问题的sort，获取该问题
     * @param questionnaireId
     * @return
     */
    @Query("from FbQuestionnaireDetail where questionnaire.id=:id and sortNo=:sortNo ")
    FbQuestionnaireDetail findDetailsBySort(@Param("id")String questionnaireId,@Param("sortNo")String sortNo);
    
    @Modifying
    @Transactional
	@Query("delete from FbQuestionnaireDetail where questionnaire.id =:questionId ")
	void deleteByQuestionId(@Param("questionId")String questionId);

    @Modifying
    @Transactional
    @Query("update FbQuestionnaireDetail set id =defaultId where questionnaire.id =:questionId ")
    void updateIdUseDefaultId(@Param("questionId")String questionId);

    @Modifying
    @Transactional
    @Query("update FbQuestionnaireDetail set defaultId =id where questionnaire.id =:questionId and defaultId is null ")
    void updateDefaultId(@Param("questionId")String questionId);

}
