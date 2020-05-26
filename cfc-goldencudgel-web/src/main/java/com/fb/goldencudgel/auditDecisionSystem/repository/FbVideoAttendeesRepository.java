package com.fb.goldencudgel.auditDecisionSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.videoConference.FbVideoAttendees;

/**
 * @author mazongjian
 * @createdDate 2019年4月22日 - 下午3:15:00 
 */
@Repository
public interface FbVideoAttendeesRepository extends JpaRepository<FbVideoAttendees, String>{

}
