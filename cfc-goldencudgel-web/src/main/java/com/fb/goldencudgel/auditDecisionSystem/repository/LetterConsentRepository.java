package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.letterConsent.FbLetterConsent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface LetterConsentRepository extends JpaRepository<FbLetterConsent,String>{

	@Query("from FbLetterConsent where compilationNo=:compilationNo and createTime <= :createTime order by createTime desc")
	List<FbLetterConsent> findLetterConsent(@Param("compilationNo") String compilationNo, @Param("createTime") Date measureDate);
}
