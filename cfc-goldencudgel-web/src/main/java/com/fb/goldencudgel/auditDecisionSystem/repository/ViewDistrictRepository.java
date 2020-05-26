package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.addreData.ViewDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewDistrictRepository extends JpaRepository<ViewDistrict,String> {

    @Query(" from ViewDistrict where cityCode =:cityCode")
   public List<ViewDistrict> findByCityCode(@Param("cityCode")String cityCode);
}
