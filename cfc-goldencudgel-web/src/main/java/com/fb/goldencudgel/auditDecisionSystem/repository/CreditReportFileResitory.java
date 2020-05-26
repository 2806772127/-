package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.creditReportingFile.FbCreditReportingFile;
import com.fb.goldencudgel.auditDecisionSystem.domain.role.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CreditReportFileResitory extends JpaRepository<FbCreditReportingFile,String> {

    @Query("from FbCreditReportingFile WHERE attachId = ?1")
    public FbCreditReportingFile findByAttachId(String attachId);
/*
    @Query("DELETE from FbCreditReportingFile WHERE attachId = ?1")
     void deleteById (String attachId);*/

}
