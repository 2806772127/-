//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.03.22 时间 02:57:43 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getSaleHomeInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>reachingRateInfo complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="reachingRateInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="applyRate" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="achieveRate" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="prodCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="prodName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="applyCount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="applyAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="achieveCount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="loanAmount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="statisticsTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "reachingRateInfo", propOrder = {
    "applyRate",
    "achieveRate",
    "prodCode",
    "prodName",
    "applyCount",
    "applyAmount",
    "achieveCount",
    "loanAmount",
    "statisticsTime"
})
public class ReachingRateInfo {

    protected Double applyRate;
    protected Double achieveRate;
    protected String prodCode;
    protected String prodName;
    protected String applyCount;
    protected String applyAmount;
    protected String achieveCount;
    protected String loanAmount;
    protected String statisticsTime;

    /**
     * 获取applyRate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getApplyRate() {
        return applyRate;
    }

    /**
     * 设置applyRate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setApplyRate(Double value) {
        this.applyRate = value;
    }

    /**
     * 获取achieveRate属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAchieveRate() {
        return achieveRate;
    }

    /**
     * 设置achieveRate属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAchieveRate(Double value) {
        this.achieveRate = value;
    }

    /**
     * 获取prodCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProdCode() {
        return prodCode;
    }

    /**
     * 设置prodCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProdCode(String value) {
        this.prodCode = value;
    }

    /**
     * 获取prodName属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProdName() {
        return prodName;
    }

    /**
     * 设置prodName属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProdName(String value) {
        this.prodName = value;
    }

    /**
     * 获取applyCount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplyCount() {
        return applyCount;
    }

    /**
     * 设置applyCount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplyCount(String value) {
        this.applyCount = value;
    }

    /**
     * 获取applyAmount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplyAmount() {
        return applyAmount;
    }

    /**
     * 设置applyAmount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplyAmount(String value) {
        this.applyAmount = value;
    }

    /**
     * 获取achieveCount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAchieveCount() {
        return achieveCount;
    }

    /**
     * 设置achieveCount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAchieveCount(String value) {
        this.achieveCount = value;
    }

    /**
     * 获取loanAmount属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanAmount() {
        return loanAmount;
    }

    /**
     * 设置loanAmount属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanAmount(String value) {
        this.loanAmount = value;
    }

    public String getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(String statisticsTime) {
        this.statisticsTime = statisticsTime;
    }
    
}
