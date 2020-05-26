//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.09.09 时间 11:11:35 AM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.companyBaseInfo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>getCompanyBaseInfoRs complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="getCompanyBaseInfoRs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="isNew" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sourceType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sourceName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compCapital" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compCreation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compTpyeName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compRegAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compContact" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="profeCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="professional" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="professionalOther" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="chargeId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="chargePerson" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="chargeSpouse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="chargeSpouseId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mobile" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referralId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referralName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referralContact" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referralDepartment" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="transList" type="{http://www.example.org/getCompanyBaseInfo}transList" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCompanyBaseInfoRs", propOrder = {
    "isNew",
    "compCode",
    "compName",
    "sourceType",
    "sourceName",
    "compCapital",
    "compCreation",
    "compType",
    "compTpyeName",
    "compRegAddress",
    "compAddress",
    "compContact",
    "profeCode",
    "professional",
    "professionalOther",
    "chargeId",
    "chargePerson",
    "chargeSpouse",
    "chargeSpouseId",
    "mobile",
    "email",
    "interviewee",
    "referralId",
    "referralName",
    "referralContact",
    "referralDepartment",
    "transList"
})
public class GetCompanyBaseInfoRs {

    @XmlElement(required = true)
    protected String isNew;
    @XmlElement(required = true)
    protected String compCode;
    @XmlElement(required = true)
    protected String compName;
    @XmlElement(required = true)
    protected String sourceType;
    @XmlElement(required = true)
    protected String sourceName;
    @XmlElement(required = true)
    protected String compCapital;
    @XmlElement(required = true)
    protected String compCreation;
    @XmlElement(required = true)
    protected String compType;
    @XmlElement(required = true)
    protected String compTpyeName;
    @XmlElement(required = true)
    protected String compRegAddress;
    @XmlElement(required = true)
    protected String compAddress;
    @XmlElement(required = true)
    protected String compContact;
    @XmlElement(required = true)
    protected String profeCode;
    @XmlElement(required = true)
    protected String professional;
    @XmlElement(required = true)
    protected String professionalOther;
    @XmlElement(required = true)
    protected String chargeId;
    @XmlElement(required = true)
    protected String chargePerson;
    @XmlElement(required = true)
    protected String chargeSpouse;
    @XmlElement(required = true)
    protected String chargeSpouseId;
    @XmlElement(required = true)
    protected String mobile;
    @XmlElement(required = true)
    protected String email;
    @XmlElement(required = true)
    protected String interviewee;
    @XmlElement(required = true)
    protected String referralId;
    @XmlElement(required = true)
    protected String referralName;
    @XmlElement(required = true)
    protected String referralContact;
    @XmlElement(required = true)
    protected String referralDepartment;
    protected List<TransList> transList;

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
     * 获取compTpyeName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompTpyeName() {
        return compTpyeName;
    }

    /**
     * 设置compTpyeName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompTpyeName(String value) {
        this.compTpyeName = value;
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
     * 获取chargeId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargeId() {
        return chargeId;
    }

    /**
     * 设置chargeId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargeId(String value) {
        this.chargeId = value;
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
     * 获取chargeSpouse属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargeSpouse() {
        return chargeSpouse;
    }

    /**
     * 设置chargeSpouse属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargeSpouse(String value) {
        this.chargeSpouse = value;
    }

    /**
     * 获取chargeSpouseId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChargeSpouseId() {
        return chargeSpouseId;
    }

    /**
     * 设置chargeSpouseId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChargeSpouseId(String value) {
        this.chargeSpouseId = value;
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
     * Gets the value of the transList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the transList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTransList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TransList }
     * 
     * 
     */
    public List<TransList> getTransList() {
        if (transList == null) {
            transList = new ArrayList<TransList>();
        }
        return this.transList;
    }

}
