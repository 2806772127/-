package com.fb.goldencudgel.auditDecisionSystem.domain.videoConference;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "VIDEO_CONFERENCE")
public class VideoConference implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CONFERENCE_ID", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String conferenceId;
	
	@Column(name = "CONFERENCE_KEYNOTE")
	private String conferenceKeyNote;
	
	@Column(name = "START_DATE")
	private Date startDate;
	
	@Column(name = "START_TIME")
	private String startTime;
	
	@Column(name = "CONFERENCE_CONTEXT")
	private String conferencecontext;
	
	@Column(name = "PARTICIPANTS")
	private String participants;
	
	@Column(name = "CREATE_USER")
	private String createUser;
	
	@Column(name = "CREATE_TIME")
	private Date creatTime;

	@Column(name = "VIDEO_ID")
	private String videoId;

	@Column(name = "CONFERENCE_PWD")
	private String conferencePwd;

	@Column(name = "WEBEX_ACCOUNT")
	private String webexAccount;
	@Column(name = "END_TIME")
	private String endTime;


	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getConferencePwd() {
		return conferencePwd;
	}

	public void setConferencePwd(String conferencePwd) {
		this.conferencePwd = conferencePwd;
	}

	public String getConferenceId() {
		return conferenceId;
	}

	public void setConferenceId(String conferenceId) {
		this.conferenceId = conferenceId;
	}

	public String getConferenceKeyNote() {
		return conferenceKeyNote;
	}

	public void setConferenceKeyNote(String conferenceKeyNote) {
		this.conferenceKeyNote = conferenceKeyNote;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getConferencecontext() {
		return conferencecontext;
	}

	public void setConferencecontext(String conferencecontext) {
		this.conferencecontext = conferencecontext;
	}

	public String getParticipants() {
		return participants;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getWebexAccount() {
		return webexAccount;
	}

	public void setWebexAccount(String webexAccount) {
		this.webexAccount = webexAccount;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
