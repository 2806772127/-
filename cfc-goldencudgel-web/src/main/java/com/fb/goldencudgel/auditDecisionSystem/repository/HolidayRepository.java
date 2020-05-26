package com.fb.goldencudgel.auditDecisionSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.holiday.Holiday;

@Repository
public interface HolidayRepository  extends JpaRepository<Holiday,String>{

	@Query("from Holiday where dateOfYear=:dateOfYear")
	List<Holiday> getHolidayByYear(@Param("dateOfYear")String year);

}
