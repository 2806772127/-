package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.abilityCompareConfig.AbilityCompareConfig;
import com.fb.goldencudgel.auditDecisionSystem.domain.ratingConfig.RatingConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingConfigRepository extends JpaRepository<RatingConfig, String> {

}
