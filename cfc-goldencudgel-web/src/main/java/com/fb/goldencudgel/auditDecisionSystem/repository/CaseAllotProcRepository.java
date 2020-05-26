package com.fb.goldencudgel.auditDecisionSystem.repository;


import com.fb.goldencudgel.auditDecisionSystem.domain.caseAllotProc.CaseAllotProc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CaseAllotProcRepository extends JpaRepository<CaseAllotProc, String> {

    @Query("select max(nullif(showOrder,0)) from CaseAllotProc where caseInfoId=:caseInfoId")
    String getMaxShowOrder(@Param("caseInfoId")String caseInfoId);
}
