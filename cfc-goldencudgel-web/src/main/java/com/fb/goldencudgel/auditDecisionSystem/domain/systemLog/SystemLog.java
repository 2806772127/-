package com.fb.goldencudgel.auditDecisionSystem.domain.systemLog;


import com.fb.goldencudgel.auditDecisionSystem.domain.token.FbTokenDetial;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
  * 
  * SYSTWM_LOG 实体类
  *
  * @date 2019-04-11 10:07:53,875
  * @author 
  */ 
@Entity
@Table(name = "SYSTWM_LOG")
public class SystemLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="ID")
	private String id;
	@Column(name ="OPERATION_TYPE")
	private String operationType;
	@Column(name ="OPERATION_USER")
	private String operationUser;
	@Column(name ="OPERATION_USER_CODE")
	private String operationUserCode;
	@Column(name ="OPERATION_IP")
	private String operationIp;
	@Column(name ="DEVICE_FINGERPRINT")
	private String deviceFingeraprint;
	@Column(name ="DEVICE_TYPE")
	private String deviceType;
	@Column(name ="OPERATION_CONTEXT")
	private String operationContext;
	@Column(name ="CREATE_TIME")
	private java.sql.Timestamp createTime;
	@Column(name ="CREATE_USER")
	private String createUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getOperationUser() {
		return operationUser;
	}

	public void setOperationUser(String operationUser) {
		this.operationUser = operationUser;
	}

	public String getOperationUserCode() {
		return operationUserCode;
	}

	public void setOperationUserCode(String operationUserCode) {
		this.operationUserCode = operationUserCode;
	}

	public String getOperationIp() {
		return operationIp;
	}

	public void setOperationIp(String operationIp) {
		this.operationIp = operationIp;
	}

	public String getDeviceFingeraprint() {
		return deviceFingeraprint;
	}

	public void setDeviceFingeraprint(String deviceFingeraprint) {
		this.deviceFingeraprint = deviceFingeraprint;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getOperationContext() {
		return operationContext;
	}

	public void setOperationContext(String operationContext) {
		this.operationContext = operationContext;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
}
