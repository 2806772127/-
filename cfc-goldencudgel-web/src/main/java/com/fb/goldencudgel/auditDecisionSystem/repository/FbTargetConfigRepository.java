package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.report.FbTargetConfig;
 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FbTargetConfigRepository extends JpaRepository<FbTargetConfig, String> {

 @Query("from FbTargetConfig where targetUserCode=:targetUserCode and targetCycle=:targetCycle ")
 public List<FbTargetConfig> findbyUserYearMon(@Param("targetUserCode")String targetUserCode, @Param("targetCycle")String targetCycle);

 @Transactional
 @Modifying
 @Query(value = "delete from FbTargetConfig where targetCycle like :targetCycle")
 void delAccount(@Param("targetCycle")String targetCycle);

}
