package com.fb.goldencudgel.auditDecisionSystem.repository;


import com.fb.goldencudgel.auditDecisionSystem.domain.itemData.ViewDataDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DataDictRepository extends JpaRepository<ViewDataDict, String> {

    @Query("from ViewDataDict where dictId=:dictId")
    List<ViewDataDict> findByDictId(@Param("dictId")String dictId);
}
