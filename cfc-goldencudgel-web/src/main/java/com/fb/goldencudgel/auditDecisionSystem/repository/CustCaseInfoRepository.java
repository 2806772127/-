package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.custCaseInfo.CustCaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * hu
 */
@Repository
public interface CustCaseInfoRepository extends JpaRepository<CustCaseInfo,String> {

    @Query(" from CustCaseInfo where caseInfoId =:caseInfoId")
    CustCaseInfo findByCaseInfoId(@Param("caseInfoId")String caseInfoId);

}
