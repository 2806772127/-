package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.queryJcicRecored.QueryJcicRecored;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryJcicRecoredRepository  extends JpaRepository<QueryJcicRecored, String> {
}
