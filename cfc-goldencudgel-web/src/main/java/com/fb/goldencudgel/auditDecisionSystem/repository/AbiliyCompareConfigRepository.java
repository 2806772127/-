package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.controller.BusinessReport.abilityConfig.AbilityConfigContronller;
import com.fb.goldencudgel.auditDecisionSystem.domain.abilityCompareConfig.AbilityCompareConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.applyIncom.FbApplyIncom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AbiliyCompareConfigRepository extends JpaRepository<AbilityCompareConfig, String> {

    @Query("from AbilityCompareConfig where configId=?1")
    AbilityCompareConfig querybyid(@Param("id")String id);
}
