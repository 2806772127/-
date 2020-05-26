package com.fb.goldencudgel.auditDecisionSystem.domain.appointMentRecord;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
  * 
  * FB_APPOINTMENT_RECORD 拜访笔记实体类
  *
  * @date 2018-12-26 10:07:53,509 
  * @author 
  */ 
@Entity
@Table(name = "FB_APPOINTMENT_RECORD")
public class FbAppointmentRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	//UUID
	@Id
	@Column(name = "APPOINTMENT_ID", unique = true, nullable = false, length = 36)
    @GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	private String appointmentId;

	//统编
	@Column(name = "COMPILATION_NO")
	private String compilationNo;

	//流水号
	@Column(name = "TRAND_ID")
	private String trandId;

	//预约日期
	@Column(name = "APPOINTMENT_DATE")
	private String appointmentDate;

	//预约时间小时
	@Column(name = "APPOINTMENT_TIME_H")
	private String appointmentTimeH;

	//预约时间分钟
	@Column(name = "APPOINTMENT_TIME_M")
	private String appointmentTimeM;

	//预约人员CODE
	@Column(name = "APPOINTMENT_USER_CODE")
	private String appointmentUserCode;

	//预约人员名称
	@Column(name = "APPOINTMENT_USER_NAME")
	private String appointmentUserName;

	//预约类型 1拜访笔记  2从征信实访
	@Column(name = "APPOINTMENT_TYPE")
	private String appointmentType;

	//客戶屬性 01：新戶 02：舊戶
	@Column(name = "COM_CUSTOMER_TYPE")
	private String comCustomerType;

	//公司名称
	@Column(name = "COMPILATION_NAME")
	private String compilationName;

	/** 實際營業地址**/
	@Column(name = "BUSINESS_ADDRESS")
	private String businessAddress;

	//拜访对象
	@Column(name = "APPOINTMENT_NAME")
	private String appointmentUser;

	/** 职称**/
	@Column(name = "APPOINTMENT_POSITION")
	private String appointmentPosition;
	//职位其他
	@Column(name = "APPOI_POSITION_OTHER")
	private String appoipositionOther;

	//公司聯絡電話區號
	@Column(name = "COM_PHONE_AREACODE")
	private String comPhoneAreaCode;

	//公司聯絡電話號碼
	@Column(name = "COM_PHONE_NUM")
	private String comPhoneNum;

	//公司聯絡電話分機
	@Column(name = "COM_PHONE_EXTEN")
	private String comPhoneExten;

	/** 案件來源：01:官网 02：自行开发 03：展期 ZJ_01：人寿转介 ZJ_02：产险转介 ZJ_03：行员转介 99：其他**/
	@Column(name = "ENTER_SOURCE")
	private String enterSource;

	/**案源轉介人员编*/
	@Column(name = "OTHER_INTRODUCE_ID")
	private  String introduceId ;
	/**案源轉介人姓名*/
	@Column(name = "OTHER_INTRODUCE_NAME")
	private  String  introduceName  ;
	/**案源轉介人單位*/
	@Column(name = "OTHER_INTRODUCE_ADDRESS")
	private  String  introduceAddress;
	/**案源轉介人電話*/
	@Column(name = "OTHER_INTRODUCE_PHONE")
	private  String introducePhone ;

	//备注
	@Column(name = "REMARK")
	private String remark;

	/**拜访对象婚姻状态*/
	@Column(name = "APPOINTMENT_MARY_STATUS")
	private String  appointmentStatus;

	//负责人
	@Column(name = "CHARGE_PERSON")
	private String chargePerson;

	//征信實訪編號
	@Column(name = "ZJ_CREDIT_NUM")
	private String zjCreditNum;
	
	//行動電話
	@Column(name = "MOBILE")
	private String mobile;
	
	//電子郵箱
	@Column(name = "EMAIL")
	private String email;

	//申請流程編號
	@Column(name = "APPLY_PROC_NUM")
	private String applyProcNum;

	@Column(name = "CREATE_TIME")
	private Date createTime;
	@Column(name = "CREATE_USER")
	private String createUser;
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	@Column(name = "UPDATE_USER")
	private String updateUser;


	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}

	public String getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getCompilationNo() {
		return compilationNo;
	}

	public void setCompilationNo(String compilationNo) {
		this.compilationNo = compilationNo;
	}

	public String getTrandId() {
		return trandId;
	}

	public void setTrandId(String trandId) {
		this.trandId = trandId;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentTimeH() {
		return appointmentTimeH;
	}

	public void setAppointmentTimeH(String appointmentTimeH) {
		this.appointmentTimeH = appointmentTimeH;
	}

	public String getAppointmentTimeM() {
		return appointmentTimeM;
	}

	public void setAppointmentTimeM(String appointmentTimeM) {
		this.appointmentTimeM = appointmentTimeM;
	}

	public String getAppointmentUserCode() {
		return appointmentUserCode;
	}

	public void setAppointmentUserCode(String appointmentUserCode) {
		this.appointmentUserCode = appointmentUserCode;
	}

	public String getAppointmentUserName() {
		return appointmentUserName;
	}

	public void setAppointmentUserName(String appointmentUserName) {
		this.appointmentUserName = appointmentUserName;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public String getCompilationName() {
		return compilationName;
	}

	public void setCompilationName(String compilationName) {
		this.compilationName = compilationName;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getAppointmentUser() {
		return appointmentUser;
	}

	public void setAppointmentUser(String appointmentUser) {
		this.appointmentUser = appointmentUser;
	}

	public String getEnterSource() {
		return enterSource;
	}

	public void setEnterSource(String enterSource) {
		this.enterSource = enterSource;
	}

	public String getIntroduceId() {
		return introduceId;
	}

	public void setIntroduceId(String introduceId) {
		this.introduceId = introduceId;
	}

	public String getIntroduceName() {
		return introduceName;
	}

	public void setIntroduceName(String introduceName) {
		this.introduceName = introduceName;
	}

	public String getIntroduceAddress() {
		return introduceAddress;
	}

	public void setIntroduceAddress(String introduceAddress) {
		this.introduceAddress = introduceAddress;
	}

	public String getIntroducePhone() {
		return introducePhone;
	}

	public void setIntroducePhone(String introducePhone) {
		this.introducePhone = introducePhone;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getAppointmentPosition() {
		return appointmentPosition;
	}

	public void setAppointmentPosition(String appointmentPosition) {
		this.appointmentPosition = appointmentPosition;
	}

	public String getComPhoneAreaCode() {
		return comPhoneAreaCode;
	}

	public void setComPhoneAreaCode(String comPhoneAreaCode) {
		this.comPhoneAreaCode = comPhoneAreaCode;
	}

	public String getComPhoneNum() {
		return comPhoneNum;
	}

	public void setComPhoneNum(String comPhoneNum) {
		this.comPhoneNum = comPhoneNum;
	}

	public String getComPhoneExten() {
		return comPhoneExten;
	}

	public void setComPhoneExten(String comPhoneExten) {
		this.comPhoneExten = comPhoneExten;
	}

	public String getComCustomerType() {
		return comCustomerType;
	}

	public void setComCustomerType(String comCustomerType) {
		this.comCustomerType = comCustomerType;
	}

	public String getChargePerson() {
		return chargePerson;
	}

	public void setChargePerson(String chargePerson) {
		this.chargePerson = chargePerson;
	}

	public String getAppoipositionOther() {
		return appoipositionOther;
	}

	public void setAppoipositionOther(String appoipositionOther) {
		this.appoipositionOther = appoipositionOther;
	}

	public String getZjCreditNum() {
		return zjCreditNum;
	}

	public void setZjCreditNum(String zjCreditNum) {
		this.zjCreditNum = zjCreditNum;
	}

	public String getApplyProcNum() {
		return applyProcNum;
	}

	public void setApplyProcNum(String applyProcNum) {
		this.applyProcNum = applyProcNum;
	}
}
