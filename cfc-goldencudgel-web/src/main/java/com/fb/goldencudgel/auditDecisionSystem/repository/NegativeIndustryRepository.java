package com.fb.goldencudgel.auditDecisionSystem.repository;
import com.fb.goldencudgel.auditDecisionSystem.domain.negativeIndustry.NegativeIndustry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;


@Repository
public interface NegativeIndustryRepository extends JpaRepository<NegativeIndustry, String> {
	
	@Query(value="SELECT NEGATIVE_ID,NEGATIVE_TYPE,NEGATIVE_NAME,NEGATIVE_SCORE,CREATE_TIME FROM FB_NEGATIVE_INDUSTRIES WHERE 1=1 AND NEGATIVE_ID=:negativeId",nativeQuery=true)
	NegativeIndustry findByNegativeId(@Param("negativeId")String negativeId);

   @Modifying
   @Transactional
   @Query(" delete from NegativeIndustry where NEGATIVE_ID=:negativeId")
   public void deleteByNegativeId(@Param("negativeId")String negativeId);
   
   @Query(value="SELECT ITEM_CODE FROM VIEW_DATA_DICT_ITEM WHERE 1=1 AND DICT_ID='NEGATIVE_INDUSTRY' AND ITEM_NAME=:itemName ",nativeQuery=true)
   public String findItemCodeByItemName(@Param("itemName")String itemName);
   
   @Modifying
   @Transactional
   @Query(" delete from NegativeIndustry where 1=1 ")
   public void deleteByAll();
   
}
