package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.fbProdQuestion.FbProdQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;

@Repository
public interface FbProdQuestionRepository extends JpaRepository<FbProdQuestion, String> {

    @Query("from FbProdQuestion where versionNum=:versionNum and prodId=:prodId")
    List<FbProdQuestion> findQuestion(@Param("versionNum") String versionNum, @Param("prodId") String prodId);
    
    @Query("from FbProdQuestion where questionId=:questionId ")
    FbProdQuestion findByquestionId(@Param("questionId") String questionId);
    
    @Query("from FbProdQuestion where QUESTION_TYPE=:questionniareType ")
    FbProdQuestion findByQuestionniareType(@Param("questionniareType") String questionniareType);
    
    @Query(value="SELECT QUESTION_TYPE FROM FB_PROD_QUESTION WHERE 1=1 AND PROD_ID=:prodId",nativeQuery=true)
    List<String> queryQuestionType(@Param("prodId")String prodId);
    
    @Query(value="SELECT QUESTION_ID FROM FB_PROD_QUESTION WHERE 1=1 AND PROD_ID=:prodId ",nativeQuery=true)
    List<String> queryQuestionByProdId(@Param("prodId")String prodId);
    
    //@Query("from FbProdQuestion where QUESTION_TYPE=:quesType ")
    //FbProdQuestion findByqueType(@Param("quesType") String quesType);
    
    
    @Query(value="SELECT QUESTION_TYPE FROM FB_PROD_QUESTION WHERE 1=1 AND VERSION_NUM=:productVersion",nativeQuery=true)
    List<String> queryTypes(@Param("productVersion")String productVersion);
    
    @Query(value="SELECT QUESTION_ID FROM FB_PROD_QUESTION WHERE 1=1 AND VERSION_NUM=:productVersion ",nativeQuery=true)
    List<String> queryProductVersion(@Param("productVersion")String productVersion);
    
    @Modifying
    @Transactional
	@Query("delete from FbProdQuestion where prodId =:prodId ")
	void deleteByProdId(@Param("prodId")String prodId);
    
    
    @Query("from FbProdQuestion where prodId=:prodId")
    List<FbProdQuestion> findQuestionByProdId(@Param("prodId") String prodId);
    
    @Query(value="SELECT QUESTION_ID FROM FB_PROD_QUESTION WHERE 1=1 AND QUESTION_TYPE=:questionType",nativeQuery=true)
    public String findQuestionIdByType(@Param("questionType") String questionType);
    
    @Query(value="SELECT STORED_FLAG FROM FB_PROD_QUESTION WHERE 1=1 AND QUESTION_TYPE=:questionType",nativeQuery=true)
    public String findStoredFlagByType(@Param("questionType") String questionType);
    
    @Query(value="SELECT QUESTION_TYPE FROM FB_PROD_QUESTION WHERE 1=1 AND STORED_FLAG<>'' AND PROD_ID=:prodId",nativeQuery=true)
    List<String> findQuestionTypeStoredFalg(@Param("prodId") String prodId);
    
    @Query(value="SELECT QUESTION_ID FROM FB_PROD_QUESTION WHERE 1=1 AND STORED_FLAG<>'' AND PROD_ID=:prodId",nativeQuery=true)
    List<String> findQuestionIdStoredFalg(@Param("prodId") String prodId);
}
