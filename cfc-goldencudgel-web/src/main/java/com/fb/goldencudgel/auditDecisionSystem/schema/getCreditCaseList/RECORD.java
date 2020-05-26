//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.03.29 时间 01:55:16 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getCreditCaseList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>RECORD complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="RECORD">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CASE_SN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CUST_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CUST_NM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG_BUILD_DT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ORG_TYPE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CAPITAL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="REG_BUS_ADDR_OTH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BUS_ADDR_OTH" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONTACT_TITLE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONTACT_TITLE_OTHER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONT_TEL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONT_TEL_CODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CONT_TEL_EXTENSION" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IS_NEW_CUST" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="REP_NM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CRD_USR_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RECORD", propOrder = {
    "casesn",
    "custid",
    "custnm",
    "orgbuilddt",
    "orgtype",
    "capital",
    "regbusaddroth",
    "busaddroth",
    "contacttitle",
    "contacttitleother",
    "conttel",
    "conttelcode",
    "conttelextension",
    "isnewcust",
    "repnm",
    "crdusrid"
})
public class RECORD {

    @XmlElement(name = "CASE_SN")
    protected String casesn;
    @XmlElement(name = "CUST_ID")
    protected String custid;
    @XmlElement(name = "CUST_NM")
    protected String custnm;
    @XmlElement(name = "ORG_BUILD_DT")
    protected String orgbuilddt;
    @XmlElement(name = "ORG_TYPE")
    protected String orgtype;
    @XmlElement(name = "CAPITAL")
    protected String capital;
    @XmlElement(name = "REG_BUS_ADDR_OTH")
    protected String regbusaddroth;
    @XmlElement(name = "BUS_ADDR_OTH")
    protected String busaddroth;
    @XmlElement(name = "CONTACT_TITLE")
    protected String contacttitle;
    @XmlElement(name = "CONTACT_TITLE_OTHER")
    protected String contacttitleother;
    @XmlElement(name = "CONT_TEL")
    protected String conttel;
    @XmlElement(name = "CONT_TEL_CODE")
    protected String conttelcode;
    @XmlElement(name = "CONT_TEL_EXTENSION")
    protected String conttelextension;
    @XmlElement(name = "IS_NEW_CUST")
    protected String isnewcust;
    @XmlElement(name = "REP_NM")
    protected String repnm;
    @XmlElement(name = "CRD_USR_ID")
    protected String crdusrid;

    /**
     * 获取casesn属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCASESN() {
        return casesn;
    }

    /**
     * 设置casesn属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCASESN(String value) {
        this.casesn = value;
    }

    /**
     * 获取custid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUSTID() {
        return custid;
    }

    /**
     * 设置custid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUSTID(String value) {
        this.custid = value;
    }

    /**
     * 获取custnm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCUSTNM() {
        return custnm;
    }

    /**
     * 设置custnm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCUSTNM(String value) {
        this.custnm = value;
    }

    /**
     * 获取orgbuilddt属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGBUILDDT() {
        return orgbuilddt;
    }

    /**
     * 设置orgbuilddt属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGBUILDDT(String value) {
        this.orgbuilddt = value;
    }

    /**
     * 获取orgtype属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getORGTYPE() {
        return orgtype;
    }

    /**
     * 设置orgtype属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setORGTYPE(String value) {
        this.orgtype = value;
    }

    /**
     * 获取capital属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAPITAL() {
        return capital;
    }

    /**
     * 设置capital属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAPITAL(String value) {
        this.capital = value;
    }

    /**
     * 获取regbusaddroth属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREGBUSADDROTH() {
        return regbusaddroth;
    }

    /**
     * 设置regbusaddroth属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREGBUSADDROTH(String value) {
        this.regbusaddroth = value;
    }

    /**
     * 获取busaddroth属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBUSADDROTH() {
        return busaddroth;
    }

    /**
     * 设置busaddroth属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBUSADDROTH(String value) {
        this.busaddroth = value;
    }

    /**
     * 获取contacttitle属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONTACTTITLE() {
        return contacttitle;
    }

    /**
     * 设置contacttitle属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONTACTTITLE(String value) {
        this.contacttitle = value;
    }

    /**
     * 获取contacttitleother属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONTACTTITLEOTHER() {
        return contacttitleother;
    }

    /**
     * 设置contacttitleother属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONTACTTITLEOTHER(String value) {
        this.contacttitleother = value;
    }

    /**
     * 获取conttel属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONTTEL() {
        return conttel;
    }

    /**
     * 设置conttel属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONTTEL(String value) {
        this.conttel = value;
    }

    /**
     * 获取conttelcode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONTTELCODE() {
        return conttelcode;
    }

    /**
     * 设置conttelcode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONTTELCODE(String value) {
        this.conttelcode = value;
    }

    /**
     * 获取conttelextension属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONTTELEXTENSION() {
        return conttelextension;
    }

    /**
     * 设置conttelextension属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONTTELEXTENSION(String value) {
        this.conttelextension = value;
    }

    /**
     * 获取isnewcust属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISNEWCUST() {
        return isnewcust;
    }

    /**
     * 设置isnewcust属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISNEWCUST(String value) {
        this.isnewcust = value;
    }

    /**
     * 获取repnm属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getREPNM() {
        return repnm;
    }

    /**
     * 设置repnm属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setREPNM(String value) {
        this.repnm = value;
    }

    /**
     * 获取crdusrid属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCRDUSRID() {
        return crdusrid;
    }

    /**
     * 设置crdusrid属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCRDUSRID(String value) {
        this.crdusrid = value;
    }

}
