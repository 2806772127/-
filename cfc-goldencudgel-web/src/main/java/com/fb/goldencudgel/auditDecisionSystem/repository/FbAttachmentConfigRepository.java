package com.fb.goldencudgel.auditDecisionSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fb.goldencudgel.auditDecisionSystem.domain.attachmentConfig.FbAttachmentConfig;

/**
 * @author mazongjian
 * @createdDate 2019年8月10日 - 下午11:35:08 
 */
public interface FbAttachmentConfigRepository extends JpaRepository<FbAttachmentConfig,String> {
    @Query("from FbAttachmentConfig WHERE attachTypeCode = ?1")
    public List<FbAttachmentConfig> findByAttachTypeCode(@Param("attachTypeCode") String attachTypeCode);
}
