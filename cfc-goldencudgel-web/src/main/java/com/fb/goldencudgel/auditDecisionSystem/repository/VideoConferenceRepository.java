package com.fb.goldencudgel.auditDecisionSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fb.goldencudgel.auditDecisionSystem.domain.videoConference.VideoConference;

import java.util.Date;
import java.util.List;

@Repository
public interface VideoConferenceRepository  extends JpaRepository<VideoConference,String>{

      @Query(value = "SELECT * from VIDEO_CONFERENCE WHERE WEBEX_ACCOUNT=?1 AND START_DATE=?2",nativeQuery = true)
     List<VideoConference> findByWebex(@Param("webex")String webex, @Param("startDate")Date startDate) ;
}
