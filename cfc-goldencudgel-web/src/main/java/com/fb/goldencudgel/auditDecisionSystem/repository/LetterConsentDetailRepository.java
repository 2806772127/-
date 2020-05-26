package com.fb.goldencudgel.auditDecisionSystem.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.letterConsent.FbLetterConsentDetail;

@Repository
public interface LetterConsentDetailRepository extends JpaRepository<FbLetterConsentDetail,String>{

	@Query("from FbLetterConsentDetail where compilationNo=:compilationNo order by letterType asc")
	List<FbLetterConsentDetail> findDetailByCompliNo(@Param("compilationNo")String compilationNo);

	@Query("from FbLetterConsentDetail where letterDetailId=:letterDetailId")
	FbLetterConsentDetail findByLetterDetailId(@Param("letterDetailId")String letterDetailId);

	@Query("from FbLetterConsentDetail where compilationNo=:compilationNo and createUser =:createUser order by letterType asc")
	List<FbLetterConsentDetail> findDetailByCompliNoAndCreatUser(@Param("compilationNo")String compilationNo, @Param("createUser")String createUser);
	
	@Query("from FbLetterConsentDetail where compilationNo=:compilationNo and createTime <= :measureDate order by createTime desc")
    List<FbLetterConsentDetail> findDetailByCompliNoAndStatus(@Param("compilationNo")String compilationNo, @Param("measureDate") Date measureDate);

	@Query("from FbLetterConsentDetail where letterId=:letterId and nullif(approverStatus,'1') in('1','2') order by letterType asc,showOrder asc")
	List<FbLetterConsentDetail> findByLetterId(@Param("letterId")String letterId);
}
