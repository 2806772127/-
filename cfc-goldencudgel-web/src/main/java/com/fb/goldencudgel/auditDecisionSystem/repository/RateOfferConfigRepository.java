package com.fb.goldencudgel.auditDecisionSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.rateOfferConfig.RateOfferConfig;

@Repository
public interface RateOfferConfigRepository extends JpaRepository<RateOfferConfig, String> {

}
