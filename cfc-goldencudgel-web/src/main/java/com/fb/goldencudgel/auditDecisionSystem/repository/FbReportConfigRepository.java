package com.fb.goldencudgel.auditDecisionSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fb.goldencudgel.auditDecisionSystem.domain.report.FbReportConfig;

/**
 * @author mazongjian
 * @createdDate 2019年3月24日 - 下午1:49:13 
 */
public interface FbReportConfigRepository extends JpaRepository<FbReportConfig, String> {
    @Query("from FbReportConfig where reportId=:reportId")
    FbReportConfig findByReportId(@Param("reportId") String reportIds);
}
