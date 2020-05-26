package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.fbZjxJcic.FbZjxJcic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FbZjxJcicRepository extends JpaRepository<FbZjxJcic,String> {

    @Query("from FbZjxJcic WHERE zjxId =:zjxId order by zjxType,qItem asc")
    public List<FbZjxJcic> findByzjxId(@Param("zjxId") String zjxId);
}
