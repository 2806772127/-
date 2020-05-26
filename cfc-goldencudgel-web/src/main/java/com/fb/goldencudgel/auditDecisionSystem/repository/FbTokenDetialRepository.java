package com.fb.goldencudgel.auditDecisionSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.token.FbTokenDetial;

@Repository
public interface FbTokenDetialRepository extends JpaRepository<FbTokenDetial,String> {

    @Query("from FbTokenDetial where userUid=:userUid")
    public FbTokenDetial findByUid(@Param("userUid")String userUid);
}
