package com.fb.goldencudgel.auditDecisionSystem.domain.message;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "FB_MESSAGE")
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MESSAGE_ID", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String messageId;
	
	@Column(name = "MESSAGE_KEYNOTE")
	private String messageKeyNote;
	
	@Column(name = "MESSAGE_TYPE")
	private String messageType;
	
	@Column(name = "MESSAGE_TYPE_NAME")
	private String messageTypeName;
	
	@Column(name = "MESSAGE_CONTEXT")
	private String messageContext;
	
	@Column(name = "SEND_TIME")
	private Date sendTime;
	
	@Column(name = "SEND_USER")
	private String sendUser;
	
	@Column(name = "ACCPECT_USER")
	private String accpectUser;
	
	@Column(name = "READ_TIME")
	private Date readTime;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageKeyNote() {
		return messageKeyNote;
	}

	public void setMessageKeyNote(String messageKeyNote) {
		this.messageKeyNote = messageKeyNote;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageTypeName() {
		return messageTypeName;
	}

	public void setMessageTypeName(String messageTypeName) {
		this.messageTypeName = messageTypeName;
	}

	public String getMessageContext() {
		return messageContext;
	}

	public void setMessageContext(String messageContext) {
		this.messageContext = messageContext;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}

	public String getAccpectUser() {
		return accpectUser;
	}

	public void setAccpectUser(String accpectUser) {
		this.accpectUser = accpectUser;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

}
