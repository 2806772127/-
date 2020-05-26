package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.addreData.ViewStreet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewStreetRepository extends JpaRepository<ViewStreet,String> {

    @Query(" from ViewStreet where districtCode =:districtCode")
    public List<ViewStreet> findByDistrictCode(@Param("districtCode")String districtCode);
}
