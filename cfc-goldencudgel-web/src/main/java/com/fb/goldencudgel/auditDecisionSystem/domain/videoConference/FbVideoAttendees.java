package com.fb.goldencudgel.auditDecisionSystem.domain.videoConference;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author mazongjian
 * @createdDate 2019年4月22日 - 下午3:15:45 
 */

@Entity
@Table(name = "FB_VIDEO_ATTENDEES")
public class FbVideoAttendees {
    
    
    @Id
    @Column(name = "UUID", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    private String uuid;
    
    @Column(name = "CONFERENCE_ID")
    private String conferenceId;
    
    @Column(name = "PARTICIPANTS_CODE")
    private String participantsCode;
    
    @Column(name = "PARTICIPANTS")
    private String participants;
    
    @Column(name = "VIDEO_ID")
    private String videoId;
    
    @Column(name = "VIDEO_STATU")   
    private String videoStatu;
    
    @Column(name = "CREATE_USER")
    private String createUser;
    
    @Column(name = "CREATE_TIME")
    private Date createTime;

    public String getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(String conferenceId) {
        this.conferenceId = conferenceId;
    }

    public String getParticipantsCode() {
        return participantsCode;
    }

    public void setParticipantsCode(String participantsCode) {
        this.participantsCode = participantsCode;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoStatu() {
        return videoStatu;
    }

    public void setVideoStatu(String videoStatu) {
        this.videoStatu = videoStatu;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
}
