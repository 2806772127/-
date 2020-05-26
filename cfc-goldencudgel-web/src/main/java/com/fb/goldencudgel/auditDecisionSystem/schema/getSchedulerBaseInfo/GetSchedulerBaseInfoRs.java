//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.08.22 时间 11:37:23 AM CST
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getSchedulerBaseInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>getSchedulerBaseInfoRs complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="getSchedulerBaseInfoRs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="compCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="isNew" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sourceType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sourceName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referralId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referralName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referralContact" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referralDepartment" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compCapital" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compCreation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compTypeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compRegAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compContact" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="interviewee" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="professional" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="professionalOther" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="profeCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="schedulerDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compRemark" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="punchCardStart" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="punchCardEnd" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="buttonCheck" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="chargePerson" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="industryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="industryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="caseNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mobile" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="altertMsg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSchedulerBaseInfoRs", propOrder = {
        "compCode",
        "isNew",
        "sourceType",
        "sourceName",
        "referralId",
        "referralName",
        "referralContact",
        "referralDepartment",
        "compName",
        "compCapital",
        "compCreation",
        "compType",
        "compTypeName",
        "compRegAddress",
        "compAddress",
        "compContact",
        "interviewee",
        "professional",
        "professionalOther",
        "profeCode",
        "schedulerDate",
        "compRemark",
        "punchCardStart",
        "punchCardEnd",
        "buttonCheck",
        "chargePerson",
        "industryCode",
        "industryName",
        "caseNo",
        "mobile",
        "email",
        "altertMsg"
})
public class GetSchedulerBaseInfoRs {

    @XmlElement(required = true)
    protected String compCode;
    @XmlElement(required = true)
    protected String isNew;
    @XmlElement(required = true)
    protected String sourceType;
    @XmlElement(required = true)
    protected String sourceName;
    @XmlElement(required = true)
    protected String referralId;
    @XmlElement(required = true)
    protected String referralName;
    @XmlElement(required = true)
    protected String referralContact;
    @XmlElement(required = true)
    protected String referralDepartment;
    @XmlElement(required = true)
    protected String compName;
    @XmlElement(required = true)
    protected String compCapital;
    @XmlElement(required = true)
    protected String compCreation;
    @XmlElement(required = true)
    protected String compType;
    @XmlElement(required = true)
    protected String compTypeName;
    @XmlElement(required = true)
    protected String compRegAddress;
    @XmlElement(required = true)
    protected String compAddress;
    @XmlElement(required = true)
    protected String compContact;
    @XmlElement(required = true)
    protected String interviewee;
    @XmlElement(required = true)
    protected String professional;
    @XmlElement(required = true)
    protected String professionalOther;
    @XmlElement(required = true)
    protected String profeCode;
    @XmlElement(required = true)
    protected String schedulerDate;
    @XmlElement(required = true)
    protected String compRemark;
    @XmlElement(required = true)
    protected String punchCardStart;
    @XmlElement(required = true)
    protected String punchCardEnd;
    @XmlElement(required = true)
    protected String buttonCheck;
    @XmlElement(required = true)
    protected String chargePerson;
    protected String industryCode;
    protected String industryName;
    @XmlElement(required = true)
    protected String caseNo;
    @XmlElement(required = true)
    protected String mobile;
    @XmlElement(required = true)
    protected String email;
    @XmlElement(required = true)
    protected String altertMsg;

    /**
     * 获取compCode属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCompCode() {
        return compCode;
    }

    /**
     * 设置compCode属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCompCode(String value) {
        this.compCode = value;
    }

    /**
     * 获取isNew属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIsNew() {
        return isNew;
    }

    /**
     * 设置isNew属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIsNew(String value) {
        this.isNew = value;
    }

    /**
     * 获取sourceType属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSourceType() {
        return sourceType;
    }

    /**
     * 设置sourceType属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSourceType(String value) {
        this.sourceType = value;
    }

    /**
     * 获取sourceName属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * 设置sourceName属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSourceName(String value) {
        this.sourceName = value;
    }

    /**
     * 获取referralId属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getReferralId() {
        return referralId;
    }

    /**
     * 设置referralId属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setReferralId(String value) {
        this.referralId = value;
    }

    /**
     * 获取referralName属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getReferralName() {
        return referralName;
    }

    /**
     * 设置referralName属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setReferralName(String value) {
        this.referralName = value;
    }

    /**
     * 获取referralContact属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getReferralContact() {
        return referralContact;
    }

    /**
     * 设置referralContact属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setReferralContact(String value) {
        this.referralContact = value;
    }

    /**
     * 获取referralDepartment属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getReferralDepartment() {
        return referralDepartment;
    }

    /**
     * 设置referralDepartment属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setReferralDepartment(String value) {
        this.referralDepartment = value;
    }

    /**
     * 获取compName属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCompName() {
        return compName;
    }

    /**
     * 设置compName属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCompName(String value) {
        this.compName = value;
    }

    /**
     * 获取compCapital属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCompCapital() {
        return compCapital;
    }

    /**
     * 设置compCapital属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCompCapital(String value) {
        this.compCapital = value;
    }

    /**
     * 获取compCreation属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCompCreation() {
        return compCreation;
    }

    /**
     * 设置compCreation属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCompCreation(String value) {
        this.compCreation = value;
    }

    /**
     * 获取compType属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCompType() {
        return compType;
    }

    /**
     * 设置compType属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCompType(String value) {
        this.compType = value;
    }

    /**
     * 获取compTypeName属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCompTypeName() {
        return compTypeName;
    }

    /**
     * 设置compTypeName属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCompTypeName(String value) {
        this.compTypeName = value;
    }

    /**
     * 获取compRegAddress属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCompRegAddress() {
        return compRegAddress;
    }

    /**
     * 设置compRegAddress属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCompRegAddress(String value) {
        this.compRegAddress = value;
    }

    /**
     * 获取compAddress属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCompAddress() {
        return compAddress;
    }

    /**
     * 设置compAddress属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCompAddress(String value) {
        this.compAddress = value;
    }

    /**
     * 获取compContact属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCompContact() {
        return compContact;
    }

    /**
     * 设置compContact属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCompContact(String value) {
        this.compContact = value;
    }

    /**
     * 获取interviewee属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getInterviewee() {
        return interviewee;
    }

    /**
     * 设置interviewee属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setInterviewee(String value) {
        this.interviewee = value;
    }

    /**
     * 获取professional属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getProfessional() {
        return professional;
    }

    /**
     * 设置professional属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setProfessional(String value) {
        this.professional = value;
    }

    /**
     * 获取professionalOther属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getProfessionalOther() {
        return professionalOther;
    }

    /**
     * 设置professionalOther属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setProfessionalOther(String value) {
        this.professionalOther = value;
    }

    /**
     * 获取profeCode属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getProfeCode() {
        return profeCode;
    }

    /**
     * 设置profeCode属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setProfeCode(String value) {
        this.profeCode = value;
    }

    /**
     * 获取schedulerDate属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSchedulerDate() {
        return schedulerDate;
    }

    /**
     * 设置schedulerDate属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSchedulerDate(String value) {
        this.schedulerDate = value;
    }

    /**
     * 获取compRemark属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCompRemark() {
        return compRemark;
    }

    /**
     * 设置compRemark属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCompRemark(String value) {
        this.compRemark = value;
    }

    /**
     * 获取punchCardStart属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPunchCardStart() {
        return punchCardStart;
    }

    /**
     * 设置punchCardStart属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPunchCardStart(String value) {
        this.punchCardStart = value;
    }

    /**
     * 获取punchCardEnd属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPunchCardEnd() {
        return punchCardEnd;
    }

    /**
     * 设置punchCardEnd属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPunchCardEnd(String value) {
        this.punchCardEnd = value;
    }

    /**
     * 获取buttonCheck属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getButtonCheck() {
        return buttonCheck;
    }

    /**
     * 设置buttonCheck属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setButtonCheck(String value) {
        this.buttonCheck = value;
    }

    /**
     * 获取chargePerson属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getChargePerson() {
        return chargePerson;
    }

    /**
     * 设置chargePerson属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setChargePerson(String value) {
        this.chargePerson = value;
    }

    /**
     * 获取industryCode属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIndustryCode() {
        return industryCode;
    }

    /**
     * 设置industryCode属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIndustryCode(String value) {
        this.industryCode = value;
    }

    /**
     * 获取industryName属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIndustryName() {
        return industryName;
    }

    /**
     * 设置industryName属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIndustryName(String value) {
        this.industryName = value;
    }

    /**
     * 获取caseNo属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCaseNo() {
        return caseNo;
    }

    /**
     * 设置caseNo属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCaseNo(String value) {
        this.caseNo = value;
    }

    /**
     * 获取mobile属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置mobile属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMobile(String value) {
        this.mobile = value;
    }

    /**
     * 获取email属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置email属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * 获取altertMsg属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAltertMsg() {
        return altertMsg;
    }

    /**
     * 设置altertMsg属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAltertMsg(String value) {
        this.altertMsg = value;
    }

}
