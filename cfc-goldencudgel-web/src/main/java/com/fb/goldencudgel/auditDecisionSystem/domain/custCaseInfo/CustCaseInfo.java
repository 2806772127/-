package com.fb.goldencudgel.auditDecisionSystem.domain.custCaseInfo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * hu
 * 客戶案件資料表
 */
@Entity
@Table(name = "FB_CUST_CASE_INFO")
public class CustCaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    //案件資料編號
    @Id
    @Column(name = "CASE_INFO_ID")
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    private String caseInfoId;

    //通路ID
    @Column(name = "CHANNEL_ID")
    private String channelId;

    //通路名稱
    @Column(name = "CHANNEL_NAME")
    private String channelName;

    //授信戶統編
    @Column(name = "COMP_CODE")
    private String compCode;

    //公司名稱
    @Column(name = "COMP_NAME")
    private String compName;

    //負責人
    @Column(name = "PRINCIPAL_NAME")
    private String principalName;

    //公司设立日期
    @Column(name = "COM_ESTABDATE")
    private Date comEstabdate;

    //组织形态
    @Column(name = "COM_ORGANIZATION")
    private String comOrganization;

    //实际资本额
    @Column(name = "COM_ACTUAL_CAPITAL")
    private String comActualCapital;

    //公司登記地址縣市別code
    @Column(name = "COMP_REG_CITY_CODE")
    private String compRegCityCode;

    //公司登記地址縣市別
    @Column(name = "COMP_REG_CITY_NAME")
    private String compRegCityName;

    //公司登記地址區域code
    @Column(name = "COMP_REG_AREA_CODE")
    private String compRegAreaCode;

    //公司登記地址區域
    @Column(name = "COMP_REG_AREA_NAME")
    private String compRegAreaName;

    //公司登記詳細地址
    @Column(name = "COMP_REG_ADDRESS")
    private String compRegAddress;

    //縣市別編號
    @Column(name = "CITY_CODE")
    private String cityCode;

    //實際營業地址縣市別
    @Column(name = "CITY_NAME")
    private String cityName;

    //實際營業地址區域code
    @Column(name = "AREA_CODE")
    private String areaCode;

    //實際營業地址區域
    @Column(name = "AREA_NAME")
    private String areaName;

    //實際營業詳細地址
    @Column(name = "COM_ADDRESS")
    private String comAddress;

    //最近一年營收(萬元)
    @Column(name = "COM_RECYEAR_REVENUE_INTERVAL")
    private String comRecyearRevenueInterval;

    //聯繫人
    @Column(name = "CONTACT_NAME")
    private String contactName;

    //稱謂
    @Column(name = "CONTACT_POSITION")
    private String contactPosition;

    //職稱_其他
    @Column(name = "CONTACT_POSITION_OTHER")
    private String contactPositionOther;

    //聯繫人電話
    @Column(name = "CONTACT_PHONE")
    private String contactPhone;

    //行動電話
    @Column(name = "CONTACT_MOBILE")
    private String contactMobile;

    //電子郵件信箱
    @Column(name = "CONTACT_EMAIL")
    private String contactEmail;

    //聯絡地址
    @Column(name = "CONTACT_ADDRESS")
    private String contactAddress;

    //產品
    @Column(name = "PROD_NAME")
    private String prodName;

    //產品類別
    @Column(name = "PROD_TYPE_NAME")
    private String prodTypeName;

    //貸款申請額度(仟元)
    @Column(name = "LOAN_APPLY_AMT")
    private String loanApplyAmt;

    //員工人數
    @Column(name = "STAFF_NUM")
    private String staffNum;

    //負責人婚姻狀況 0:未婚 1:已婚 2:離異 3:喪偶
    @Column(name = "MARRIAGE_STATUS")
    private String marriageStatus;

    //授信戶、負責人、配偶及其子女不動產持有情形有無不動產 01:無不動產 02:有不動產且無銀行貸款 03:有不動產但有銀行貸款
    @Column(name = "HAS_REAL_ESTATE_FLAG")
    private String hasRealEstateFlag;

    //請問你從哪裡得知本產品訊息: 01:Yahoo搜尋 02:Google搜尋 03:本行官網 04:本行產品DM 05:本行客戶 06:親友推薦 07:電台廣播 99:其他
    @Column(name = "ENTER_SOURCE")
    private String enterSource;

    //推薦方式:01:官網 02:自行開發 03:展期 99:其他 JH_01:績展 ZJ_01:人壽轉介 ZJ_02:產險轉介
    @Column(name = "SOURCE_TYPE")
    private String sourceType;

    //案源轉介人员编
    @Column(name = "OTHER_INTRODUCE_ID")
    private String otherIntroduceId;

    //案源轉介人姓名
    @Column(name = "OTHER_INTRODUCE_NAME")
    private String otherIntroduceName;

    //申請時間
    @Column(name = "APPLY_TIME")
    private Date applyTime;

    //留言或備註
    @Column(name = "REMARK")
    private String remark;

    //經辦人員編
    @Column(name = "AGENT_USER_CODE")
    private String agentUserCode;

    //經辦人姓名
    @Column(name = "AGENT_USER_NAME")
    private String agentUserName;

    //經辦人組長員編
    @Column(name = "AGENT_TEAM_CODE")
    private String agentTeamCode;

    //經辦人組長姓名
    @Column(name = "AGENT_TEAM_NAME")
    private String agentTeamName;

    //分配業務組別
    @Column(name = "ALLOT_GROUP_CODE")
    private String allotGroupCode;

    //分配方式 1:自動分配 2:人工分配
    @Column(name = "ALLOT_TYPE")
    private String allotType;

    //分配狀態 1:未分配 2:已分配(組長) 3:已分配(業務員) 4:列為黑名單'
    @Column(name = "ALLOT_STATUS")
    private String allotStatus;

    //黑名單備註
    @Column(name = "BLACK_REMARK")
    private String blackRemark;

    //分配時間
    @Column(name = "ALLOT_TIME")
    private Date allotTime;

    //分配给組長時間
    @Column(name = "ALLOT_TEAM_TIME")
    private Date allotTeamTime;

    //分配用戶名
    @Column(name = "ALLOT_USER")
    private String allotUser;

    //處理標註： 1為處理 2已處理
    @Column(name = "HANDLE_FLAG")
    private String handleFlag;

    //處理標註時間
    @Column(name = "HANDLE_TIME")
    private Date handleTime;

    //申請流程編號
    @Column(name = "APPLY_PROC_NUM")
    private String applyProcNum;

    public String getCaseInfoId() {
        return caseInfoId;
    }

    public void setCaseInfoId(String caseInfoId) {
        this.caseInfoId = caseInfoId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdTypeName() {
        return prodTypeName;
    }

    public void setProdTypeName(String prodTypeName) {
        this.prodTypeName = prodTypeName;
    }

    public String getLoanApplyAmt() {
        return loanApplyAmt;
    }

    public void setLoanApplyAmt(String loanApplyAmt) {
        this.loanApplyAmt = loanApplyAmt;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public String getAllotType() {
        return allotType;
    }

    public void setAllotType(String allotType) {
        this.allotType = allotType;
    }

    public String getAllotStatus() {
        return allotStatus;
    }

    public void setAllotStatus(String allotStatus) {
        this.allotStatus = allotStatus;
    }

    public Date getAllotTime() {
        return allotTime;
    }

    public void setAllotTime(Date allotTime) {
        this.allotTime = allotTime;
    }

    public String getAllotUser() {
        return allotUser;
    }

    public void setAllotUser(String allotUser) {
        this.allotUser = allotUser;
    }

    public String getApplyProcNum() {
        return applyProcNum;
    }

    public void setApplyProcNum(String applyProcNum) {
        this.applyProcNum = applyProcNum;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public Date getComEstabdate() {
        return comEstabdate;
    }

    public void setComEstabdate(Date comEstabdate) {
        this.comEstabdate = comEstabdate;
    }

    public String getComOrganization() {
        return comOrganization;
    }

    public void setComOrganization(String comOrganization) {
        this.comOrganization = comOrganization;
    }

    public String getComActualCapital() {
        return comActualCapital;
    }

    public void setComActualCapital(String comActualCapital) {
        this.comActualCapital = comActualCapital;
    }

    public String getCompRegCityCode() {
        return compRegCityCode;
    }

    public void setCompRegCityCode(String compRegCityCode) {
        this.compRegCityCode = compRegCityCode;
    }

    public String getCompRegCityName() {
        return compRegCityName;
    }

    public void setCompRegCityName(String compRegCityName) {
        this.compRegCityName = compRegCityName;
    }

    public String getCompRegAreaCode() {
        return compRegAreaCode;
    }

    public void setCompRegAreaCode(String compRegAreaCode) {
        this.compRegAreaCode = compRegAreaCode;
    }

    public String getCompRegAreaName() {
        return compRegAreaName;
    }

    public void setCompRegAreaName(String compRegAreaName) {
        this.compRegAreaName = compRegAreaName;
    }

    public String getCompRegAddress() {
        return compRegAddress;
    }

    public void setCompRegAddress(String compRegAddress) {
        this.compRegAddress = compRegAddress;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getComAddress() {
        return comAddress;
    }

    public void setComAddress(String comAddress) {
        this.comAddress = comAddress;
    }

    public String getComRecyearRevenueInterval() {
        return comRecyearRevenueInterval;
    }

    public void setComRecyearRevenueInterval(String comRecyearRevenueInterval) {
        this.comRecyearRevenueInterval = comRecyearRevenueInterval;
    }

    public String getContactPosition() {
        return contactPosition;
    }

    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getEnterSource() {
        return enterSource;
    }

    public void setEnterSource(String enterSource) {
        this.enterSource = enterSource;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBlackRemark() {
        return blackRemark;
    }

    public void setBlackRemark(String blackRemark) {
        this.blackRemark = blackRemark;
    }

    public String getAgentTeamCode() {
        return agentTeamCode;
    }

    public void setAgentTeamCode(String agentTeamCode) {
        this.agentTeamCode = agentTeamCode;
    }

    public String getAgentTeamName() {
        return agentTeamName;
    }

    public void setAgentTeamName(String agentTeamName) {
        this.agentTeamName = agentTeamName;
    }

    public Date getAllotTeamTime() {
        return allotTeamTime;
    }

    public void setAllotTeamTime(Date allotTeamTime) {
        this.allotTeamTime = allotTeamTime;
    }

    public String getStaffNum() {
        return staffNum;
    }

    public void setStaffNum(String staffNum) {
        this.staffNum = staffNum;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public String getHasRealEstateFlag() {
        return hasRealEstateFlag;
    }

    public void setHasRealEstateFlag(String hasRealEstateFlag) {
        this.hasRealEstateFlag = hasRealEstateFlag;
    }

    public String getAllotGroupCode() {
        return allotGroupCode;
    }

    public void setAllotGroupCode(String allotGroupCode) {
        this.allotGroupCode = allotGroupCode;
    }

    public String getHandleFlag() {
        return handleFlag;
    }

    public void setHandleFlag(String handleFlag) {
        this.handleFlag = handleFlag;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getContactPositionOther() {
        return contactPositionOther;
    }

    public void setContactPositionOther(String contactPositionOther) {
        this.contactPositionOther = contactPositionOther;
    }

    public String getOtherIntroduceId() {
        return otherIntroduceId;
    }

    public void setOtherIntroduceId(String otherIntroduceId) {
        this.otherIntroduceId = otherIntroduceId;
    }

    public String getOtherIntroduceName() {
        return otherIntroduceName;
    }

    public void setOtherIntroduceName(String otherIntroduceName) {
        this.otherIntroduceName = otherIntroduceName;
    }
}
