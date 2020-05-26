package com.fb.goldencudgel.auditDecisionSystem.repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.addreData.ViewCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewCityRepository extends JpaRepository<ViewCity,String> {

}
