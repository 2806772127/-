package com.fb.goldencudgel.auditDecisionSystem.domain.user;



import javax.persistence.*;
import java.io.Serializable;

/**
  * 
  * USER_LIST 实体类
  *
  * @date 2018-12-26 10:07:53,875 
  * @author 
  */ 
@Entity
@Table(name = "USER_LIST")
@IdClass(UserListPK.class)
public class UserList implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "USER_NAME")
	private String userName;
	@Id
	@Column(name = "USER_CODE")
	private String userCode;
	@Id
	@Column(name = "USER_AREA")
	private String userArea;
	@Column(name = "USER_AREA_NAME")
	private String userAreaName;
	@Id
	@Column(name = "USER_GROUP")
	private String userGroup;
	@Column(name = "USER_GROUP_NAME")
	private String userGroupName;
	@Column(name = "ROLE_ID")
	private String roleId;
	@Column(name = "USER_TYPE")
	private String userType;
	@Column(name = "LEAVE_FLAG")
	private String leaveFlag;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserArea(String userArea) {
		this.userArea = userArea;
	}

	public String getUserArea() {
		return userArea;
	}

	public void setUserAreaName(String userAreaName) {
		this.userAreaName = userAreaName;
	}

	public String getUserAreaName() {
		return userAreaName;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserType() {
		return userType;
	}

	public String getLeaveFlag() {
		return leaveFlag;
	}

	public void setLeaveFlag(String leaveFlag) {
		this.leaveFlag = leaveFlag;
	}
}