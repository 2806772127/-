package com.fb.goldencudgel.auditDecisionSystem.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.fbWebexConfig.FbWebexConfig;

@Repository
public interface FbWebexConfigRepository extends JpaRepository<FbWebexConfig, String> {

}
