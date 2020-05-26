package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.visit.FbPunchCardRecode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FbPunchCardRecodeRepository extends JpaRepository<FbPunchCardRecode,String> {
    @Query("from FbPunchCardRecode where punchCardId=:punchCardId  ")
    List<FbPunchCardRecode> findPunchRecodeByPunchId(@Param("punchCardId")String punchCardId);


    @Query("from FbPunchCardRecode where compilationNo=:compilationNo and trandId=:trandId ")
    List<FbPunchCardRecode> findPunchRecodeList(@Param("compilationNo")String compilationNo,@Param("trandId")String trandId);
}
