package com.fb.goldencudgel.auditDecisionSystem.domain.attachment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
  * 
  * FB_ATTACHMENT 实体类
  *
  * @date 2018-12-26 10:29:36,578 
  * @author zou
  */ 
@Entity
@Table(name = "FB_ATTACHMENT")
public class FbAttachment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id

	@Column(name = "ATTACH_ID", unique = true, nullable = false, length = 36)
	private String attachId;
	@Column(name = "COMPILATION_NO")
	private String compilationNo;
	@Column(name = "ATTACH_TYPE_CODE")
	private String attachTypeCode;
	@Column(name = "ATTACH_TYPE_NAME")
	private String attachTypeName;
	@Column(name = "FILE_CONTEXT_ID")
	private String fileContextId;
	@Column(name = "FILE_STATUS")
	private String fileStatus;
	@Column(name = "FILE_PATH")
	private String filePath;
	@Column(name = "DELETE_FLAG")
	private Double deleteFlag;
	@Column(name = "FILE_CATEGORY")
	private Double fileCategory;
	@Column(name = "CREATE_TIME")
	private Date createTime;
	@Column(name = "CREATE_USER")
	private String createUser;
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	@Column(name = "UPDATE_USER")
	private String updateUser;
	@Column(name = "ATTACH_NAME_CODE")
	private String attactNameCode;
	@Column(name = "ATTACH_NAME")
	private String attactName;
	@Column(name = "FILE_NAME")
	private String fileName;
	@Column(name = "ATTACH_URL")
	private String attachUrl;

	public void setAttachId(String attachId){
		this.attachId = attachId;
	}
	public String getAttachId(){
		return attachId;
	}
	public void setCompilationNo(String compilationNo){
		this.compilationNo = compilationNo;
	}
	public String getCompilationNo(){
		return compilationNo;
	}
	public void setAttachTypeCode(String attachTypeCode){
		this.attachTypeCode = attachTypeCode;
	}
	public String getAttachTypeCode(){
		return attachTypeCode;
	}
	public void setAttachTypeName(String attachTypeName){
		this.attachTypeName = attachTypeName;
	}
	public String getAttachTypeName(){
		return attachTypeName;
	}
	public void setFileContextId(String fileContextId){
		this.fileContextId = fileContextId;
	}
	public String getFileContextId(){
		return fileContextId;
	}
	public void setFileStatus(String fileStatus){
		this.fileStatus = fileStatus;
	}
	public String getFileStatus(){
		return fileStatus;
	}
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	public String getFilePath(){
		return filePath;
	}
	public void setDeleteFlag(Double deleteFlag){
		this.deleteFlag = deleteFlag;
	}
	public Double getDeleteFlag(){
		return deleteFlag;
	}
	public void setFileCategory(Double fileCategory){
		this.fileCategory = fileCategory;
	}
	public Double getFileCategory(){
		return fileCategory;
	}
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setCreateUser(String createUser){
		this.createUser = createUser;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	public void setUpdateUser(String updateUser){
		this.updateUser = updateUser;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public String getAttactNameCode() {
		return attactNameCode;
	}
	public void setAttactNameCode(String attactNameCode) {
		this.attactNameCode = attactNameCode;
	}
	public String getAttactName() {
		return attactName;
	}
	public void setAttactName(String attactName) {
		this.attactName = attactName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getAttachUrl() { return attachUrl; }
	public void setAttachUrl(String attachUrl) { this.attachUrl = attachUrl; }
}
