package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.appointMentRecord.FbAppointmentRecord;
import com.fb.goldencudgel.auditDecisionSystem.domain.attachment.FbAttachment;
import com.fb.goldencudgel.auditDecisionSystem.domain.creditReportingFile.FbCreditReportingFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.beans.Transient;

@Repository
public interface FbAttachmentResitory extends JpaRepository<FbAttachment,String> {

    @Query("from FbAttachment WHERE fileContextId = ?1")
    public FbAttachment findByFileContextId(@Param("fileContextId")String fileContextId);

    @Modifying

    @Query("delete from FbAttachment WHERE fileContextId =:deId")
    void deleteByFileId(@Param("deId")String deId);
    
    @Query("from FbAppointmentRecord where appointmentId = :appointmentId")
    FbAppointmentRecord findByAppointmentId(@Param("appointmentId") String appointmentId);
}
