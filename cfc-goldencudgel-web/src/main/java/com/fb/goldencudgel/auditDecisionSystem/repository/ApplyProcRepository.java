package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.applyProc.ApplyProc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * hu
 */
@Repository
public interface ApplyProcRepository extends JpaRepository<ApplyProc, String> {

    @Query("from ApplyProc where compCode =:compCode and procStatus <> '03'")
    public List<ApplyProc> findByCompCode(@Param("compCode")String compCode);

    @Query("select ap from ApplyProc ap inner join CustCaseInfo cci on cci.applyProcNum = ap.appyProcNum where cci.caseInfoId in(:caseInfoId) and ap.procStatus <> '01'")
    List<ApplyProc> findInProcessCase(@Param("caseInfoId")List<String> caseInfoIdList);

    @Query("select applyProdCode from ApplyProc where compCode =:compCode and procStatus <> '03'")
    public String getApplyProdCode(@Param("compCode")String compCode);

    @Query("from ApplyProc where appyProcNum =:appyProcNum ")
    ApplyProc findAppyProcNum(@Param("appyProcNum")String appyProcNum);
}
