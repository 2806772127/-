package com.fb.goldencudgel.auditDecisionSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.fb.goldencudgel.auditDecisionSystem.domain.incatorsConfig.IncatorsConfig;

public interface IncatorsConfigRepository extends JpaRepository<IncatorsConfig, String> {

    @Query("from IncatorsConfig where configId=?1")
    IncatorsConfig querybyid(@Param("id")String id);
}