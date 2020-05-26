package com.fb.goldencudgel.auditDecisionSystem.repository;


import com.fb.goldencudgel.auditDecisionSystem.domain.fbYearAppropriationRate.FbYearAppropriationRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;




@Repository
public interface FbYearAppropriationRateRepository extends JpaRepository<FbYearAppropriationRate, String> {
	
	 @Transactional
	 @Modifying
	 @Query(value = " delete from FbYearAppropriationRate where 1=1 ")
	 void delBeforeRecord();

}
