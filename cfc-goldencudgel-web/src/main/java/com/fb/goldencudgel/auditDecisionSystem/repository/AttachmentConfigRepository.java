package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.attachmentConfig.FbAttachmentConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AttachmentConfigRepository extends JpaRepository<FbAttachmentConfig,String> {

    @Query(" from FbAttachmentConfig where nodeCode=:nodeCode and industryType=:industryType and checkItem=:checkItem and attachTypeCode=:attachTypeCode ")
    public FbAttachmentConfig findByIds(@Param("nodeCode")String nodeCode,@Param("industryType")String industryType,@Param("checkItem")String checkItem,@Param("attachTypeCode")String attachTypeCode);

    @Query(" from FbAttachmentConfig where nodeCode = :nodeCode and industryType = :industryType and attachTypeCode = :attachTypeCode and attactNameCode = :attactNameCode")
    public FbAttachmentConfig findByConditions(@Param("nodeCode") String nodeCode, @Param("industryType") String industryType, @Param("attachTypeCode") String attachTypeCode, @Param("attactNameCode") String attactNameCode);
    
    @Modifying
    @Transactional
    @Query(" delete from FbAttachmentConfig where attachTypeId=:attachTypeId")
    public void deleteById(@Param("attachTypeId")String attachTypeId);

    @Query(" from FbAttachmentConfig where attachTypeId=:attachTypeId")
    public FbAttachmentConfig findByAttachTypeId(@Param("attachTypeId")String attachTypeId);


}
