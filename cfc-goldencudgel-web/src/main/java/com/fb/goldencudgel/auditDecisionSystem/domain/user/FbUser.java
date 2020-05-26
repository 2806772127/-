package com.fb.goldencudgel.auditDecisionSystem.domain.user;


import com.fb.goldencudgel.auditDecisionSystem.domain.token.FbTokenDetial;

import java.io.Serializable;

import javax.persistence.*;

/**
  * 
  * FB_USER 实体类
  *
  * @date 2018-12-26 10:07:53,875 
  * @author 
  */ 
@Entity
@Table(name = "FB_USER")
public class FbUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name ="ACCOUNT")
	private String account;
	@Column(name ="USER_NAME")
	private String userName;
	@Column(name ="USER_CODE")
	private String userCode;
	@Column(name ="PASSWORD")
	private String password;
	@Column(name ="USER_UID")
	private String userUid;
	@Column(name ="USER_AREA")
	private String userArea;
	@Column(name ="USER_AREA_NAME")
	private String userAreaName;
	@Column(name ="USER_GROUP")
	private String userGroup;
	@Column(name ="USER_GROUP_NAME")
	private String userGroupName;
	@Column(name ="USER_DEPARTMENT")
	private String userDepartment;
	@Column(name ="USER_OFFICE")
	private String userOffice;
	@Column(name ="USER_COMPANY")
	private String userCompany;
	@Column(name ="USER_TYPE")
	private String userType;
	@Column(name ="MAIN_UNIT")
	private String mainUnit;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Column(name ="ROLE_ID")
	private String roleId;
	@Column(name ="MESSAGE_COUNT")
	private Integer messageCount;
	@Column(name ="LOGIN_TYPE")
	private String loginType;

	@Transient
	private FbTokenDetial tokenDetial;

	public void setAccount(String account){
		this.account = account;
	}
	public String getAccount(){
		return account;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}
	public String getUserName(){
		return userName;
	}
	public void setUserCode(String userCode){
		this.userCode = userCode;
	}
	public String getUserCode(){
		return userCode;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getPassword(){
		return password;
	}
	public void setUserUid(String userUid){
		this.userUid = userUid;
	}
	public String getUserUid(){
		return userUid;
	}
	public void setUserArea(String userArea){
		this.userArea = userArea;
	}
	public String getUserArea(){
		return userArea;
	}
	public void setUserAreaName(String userAreaName){
		this.userAreaName = userAreaName;
	}
	public String getUserAreaName(){
		return userAreaName;
	}
	public void setUserGroup(String userGroup){
		this.userGroup = userGroup;
	}
	public String getUserGroup(){
		return userGroup;
	}
	public void setUserGroupName(String userGroupName){
		this.userGroupName = userGroupName;
	}
	public String getUserGroupName(){
		return userGroupName;
	}
	public void setUserDepartment(String userDepartment){
		this.userDepartment = userDepartment;
	}
	public String getUserDepartment(){
		return userDepartment;
	}
	public void setUserOffice(String userOffice){
		this.userOffice = userOffice;
	}
	public String getUserOffice(){
		return userOffice;
	}
	public void setUserCompany(String userCompany){
		this.userCompany = userCompany;
	}
	public String getUserCompany(){
		return userCompany;
	}
	public void setUserType(String userType){
		this.userType = userType;
	}
	public String getUserType(){
		return userType;
	}
	public void setMessageCount(Integer messageCount){
		this.messageCount = messageCount;
	}
	public Integer getMessageCount(){
		return messageCount;
	}
	public void setLoginType(String loginType){
		this.loginType = loginType;
	}
	public String getLoginType(){
		return loginType;
	}

	public FbTokenDetial getTokenDetial() {
		return tokenDetial;
	}

	public void setTokenDetial(FbTokenDetial tokenDetial) {
		this.tokenDetial = tokenDetial;
	}

	public String getMainUnit() {
		return mainUnit;
	}

	public void setMainUnit(String mainUnit) {
		this.mainUnit = mainUnit;
	}
}
