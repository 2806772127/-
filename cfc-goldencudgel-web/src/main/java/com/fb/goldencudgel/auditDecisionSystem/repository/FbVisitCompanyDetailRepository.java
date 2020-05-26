package com.fb.goldencudgel.auditDecisionSystem.repository;


import com.fb.goldencudgel.auditDecisionSystem.domain.appointMentRecord.FbAppointmentRecord;
import com.fb.goldencudgel.auditDecisionSystem.domain.visit.FbVisitCompanyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FbVisitCompanyDetailRepository extends JpaRepository<FbVisitCompanyDetail, String> {
    /**根据公司统编和拜访日期查询*/
    @Query("from FbVisitCompanyDetail where compilationNo=:compilationNo and trandId=:trandId")
    FbVisitCompanyDetail findByNoAndId(@Param("compilationNo")String compilationNo, @Param("trandId")String trandId);
    /**根据拜访Id查询*/
    @Query("from FbVisitCompanyDetail where visitId=:visitId")
    FbVisitCompanyDetail findByVisitId(@Param("visitId")String visitId );
    
    
}
