package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.applyIncom.FbApplyIncom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyIncomRepository extends JpaRepository<FbApplyIncom, String> {

    @Query("from FbApplyIncom where compilationNo =:compilationNo and trandId =:trandId")
    public FbApplyIncom findByIds(@Param("compilationNo")String compilationNo, @Param("trandId")String trandId);
}
