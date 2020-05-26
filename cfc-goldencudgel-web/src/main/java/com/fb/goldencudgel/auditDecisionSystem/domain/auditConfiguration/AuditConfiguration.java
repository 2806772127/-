package com.fb.goldencudgel.auditDecisionSystem.domain.auditConfiguration;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "FB_APPROVE_CONFIG")
public class AuditConfiguration {

	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@Column(name = "APPROVE_ID")
	private String approveId;
	@Column(name = "FUNCTION_CODE")
	private String functionCode;
	@Column(name = "FUNCTION_NAME")
	private String functionName;
	@Column(name = "AGENT_USER_CODE")
	private String agentUserCode;
	@Column(name = "AGENT_USER_NAME")
	private String agentUserName;
	@Column(name = "APPROVE_USER_CODE")
	private String approveUserCode;
	@Column(name = "APPROVE_USER_NAME")
	private String approveUserName;
	@Column(name = "CREATE_USER")
	private String createUser;
	@Column(name = "UPDATE_USER")
	private String updateUser;
    @Column(name = "CREATE_TIME")
    private Date createTime;
    @Column(name = "UPDATE_TIME")
    private Date updateTime;
    @Column(name = "AGENT_USER_AREA")
	private String agentUserArea;
    @Column(name = "AGENT_USER_GROUP")
	private String agentUserGroup;
    @Column(name = "APPROVE_USER_AREA")
   	private String approveUserArea;
    @Column(name = "APPROVE_USER_GROUP")
   	private String approveUserGroup;
    
	public String getApproveId() {
		return approveId;
	}
	public void setApproveId(String approveId) {
		this.approveId = approveId;
	}
	public String getFunctionCode() {
		return functionCode;
	}
	public void setFunctionCode(String functionCode) {
		this.functionCode = functionCode;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getAgentUserCode() {
		return agentUserCode;
	}
	public void setAgentUserCode(String agentUserCode) {
		this.agentUserCode = agentUserCode;
	}
	public String getAgentUserName() {
		return agentUserName;
	}
	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
	}
	public String getApproveUserCode() {
		return approveUserCode;
	}
	public void setApproveUserCode(String approveUserCode) {
		this.approveUserCode = approveUserCode;
	}
	public String getApproveUserName() {
		return approveUserName;
	}
	public void setApproveUserName(String approveUserName) {
		this.approveUserName = approveUserName;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getAgentUserArea() {
		return agentUserArea;
	}
	public void setAgentUserArea(String agentUserArea) {
		this.agentUserArea = agentUserArea;
	}
	public String getAgentUserGroup() {
		return agentUserGroup;
	}
	public void setAgentUserGroup(String agentUserGroup) {
		this.agentUserGroup = agentUserGroup;
	}
	public String getApproveUserArea() {
		return approveUserArea;
	}
	public void setApproveUserArea(String approveUserArea) {
		this.approveUserArea = approveUserArea;
	}
	public String getApproveUserGroup() {
		return approveUserGroup;
	}
	public void setApproveUserGroup(String approveUserGroup) {
		this.approveUserGroup = approveUserGroup;
	}
}
