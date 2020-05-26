package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.measureWord.question.FbQuestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface QuestionnaireRepository  extends JpaRepository<FbQuestionnaire, String> {

	@Query("from FbQuestionnaire where id=:id")
	FbQuestionnaire findByQuestionnaireId(@Param("id")String questionnaireId);

    @Query("from FbQuestionnaire where questionniareType in (:questionniareTypes) and isEnable='1' ")
    List<FbQuestionnaire> findByTypes(@Param("questionniareTypes") List<String> questionniareTypes);

    @Query("from FbQuestionnaire where questionniareType =:questionniareType and isEnable='1' ")
    FbQuestionnaire findByType(@Param("questionniareType") String questionniareType);

	@Modifying
    @Transactional
    @Query("update FbQuestionnaire set isEnable=:isEnable where questionniareType =:questionniareType")
    void updateEnableStatus(@Param("isEnable")String isEnable,@Param("questionniareType")String questionniareType);

    @Modifying
    @Transactional
    @Query("update FbQuestionnaire set isEnable=:isEnable where id =:id")
    void updateStatusById(@Param("isEnable")String isEnable,@Param("id")String id);
    
    
    @Query(value="SELECT ID FROM FB_QUESTIONNAIRE WHERE 1=1 AND IS_ENABLE = '1' AND QUESTIONNAIRE_TYPE=:pCode",nativeQuery=true)
    String findPcode(@Param("pCode") String pCode);
    
    @Query(value="SELECT QUESTIONNAIRE_TYPE FROM FB_QUESTIONNAIRE WHERE 1=1 AND IS_ENABLE = '1' AND ID=:commitType",nativeQuery=true)
    String findCommitType(@Param("commitType") String commitType);
    
    @Query(value="SELECT QUESTIONNAIRE_TYPE FROM FB_QUESTIONNAIRE WHERE 1=1 AND ID=:commitType",nativeQuery=true)
    String findTypeById(@Param("commitType") String commitType);
    
    @Query("from FbQuestionnaire where questionniareType =:questionniareType and auditStatus='02' ")
    List<FbQuestionnaire> findInAuditByType(@Param("questionniareType") String questionniareType);
    
    @Query(value="SELECT AUDIT_STATUS FROM FB_QUESTIONNAIRE WHERE 1=1 AND CREATE_USER_CODE=:agentUserCode AND QUESTION_TYPE=:queType",nativeQuery=true)
    List<String> findByCreateUserCode(@Param("agentUserCode") String agentUserCode,@Param("queType") String queType);

}
