package com.fb.goldencudgel.auditDecisionSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.appointMentRecord.FbAppointmentRecord;

/**
 * @Auther hu
 */
@Repository
public interface AppointmentRecordRepository extends JpaRepository<FbAppointmentRecord, String> {

    @Query("from FbAppointmentRecord where compilationNo=:compilationNo and trandId=:trandId and appointmentType=:appointmentType")
    FbAppointmentRecord findByIds(@Param("compilationNo")String compilationNo, @Param("trandId")String trandId, @Param("appointmentType")String appointmentType);

    @Query("from FbAppointmentRecord where compilationNo=:compilationNo order by trandId")
    List<FbAppointmentRecord> findByComNo(@Param("compilationNo")String compilationNo);
    
    @Query("from FbAppointmentRecord where appointmentId = :appointmentId")
    FbAppointmentRecord findByAppointmentId(@Param("appointmentId") String appointmentId);

    @Query("from FbAppointmentRecord where compilationNo=:compilationNo and trandId=:trandId")
    FbAppointmentRecord findByComNoTrandId(@Param("compilationNo")String compilationNo, @Param("trandId")String trandId);

    @Query("from FbAppointmentRecord where applyProcNum=:applyProcNum ")
    List<FbAppointmentRecord> findByApplyProcNum(@Param("applyProcNum")String applyProcNum);
    
}
