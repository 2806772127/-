//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.03.22 时间 02:57:43 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getSaleHomeInfo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>getSaleHomeInfoRs complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="getSaleHomeInfoRs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="reachingRateInfo" type="{http://www.example.org/getSaleHomeInfo}reachingRateInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="visitModuleEnable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="agreeModuleEnable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="analyModuleEnable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="applyModuleEnable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interviewModuleEnable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="visitScheduler" type="{http://www.example.org/getSaleHomeInfo}visitScheduler" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="caseProgress" type="{http://www.example.org/getSaleHomeInfo}caseProgress" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSaleHomeInfoRs", propOrder = {
    "reachingRateInfo",
    "visitModuleEnable",
    "agreeModuleEnable",
    "analyModuleEnable",
    "applyModuleEnable",
    "interviewModuleEnable",
    "visitScheduler",
    "caseProgress"
})
public class GetSaleHomeInfoRs {

    protected List<ReachingRateInfo> reachingRateInfo;
    protected String visitModuleEnable;
    protected String agreeModuleEnable;
    protected String analyModuleEnable;
    protected String applyModuleEnable;
    protected String interviewModuleEnable;
    protected List<VisitScheduler> visitScheduler;
    protected List<CaseProgress> caseProgress;

    /**
     * Gets the value of the reachingRateInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the reachingRateInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReachingRateInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReachingRateInfo }
     * 
     * 
     */
    public List<ReachingRateInfo> getReachingRateInfo() {
        if (reachingRateInfo == null) {
            reachingRateInfo = new ArrayList<ReachingRateInfo>();
        }
        return this.reachingRateInfo;
    }

    /**
     * 获取visitModuleEnable属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVisitModuleEnable() {
        return visitModuleEnable;
    }

    /**
     * 设置visitModuleEnable属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVisitModuleEnable(String value) {
        this.visitModuleEnable = value;
    }

    /**
     * 获取agreeModuleEnable属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgreeModuleEnable() {
        return agreeModuleEnable;
    }

    /**
     * 设置agreeModuleEnable属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgreeModuleEnable(String value) {
        this.agreeModuleEnable = value;
    }

    /**
     * 获取analyModuleEnable属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalyModuleEnable() {
        return analyModuleEnable;
    }

    /**
     * 设置analyModuleEnable属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalyModuleEnable(String value) {
        this.analyModuleEnable = value;
    }

    /**
     * 获取applyModuleEnable属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplyModuleEnable() {
        return applyModuleEnable;
    }

    /**
     * 设置applyModuleEnable属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplyModuleEnable(String value) {
        this.applyModuleEnable = value;
    }

    /**
     * 获取interviewModuleEnable属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterviewModuleEnable() {
        return interviewModuleEnable;
    }

    /**
     * 设置interviewModuleEnable属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterviewModuleEnable(String value) {
        this.interviewModuleEnable = value;
    }

    /**
     * Gets the value of the visitScheduler property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the visitScheduler property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVisitScheduler().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VisitScheduler }
     * 
     * 
     */
    public List<VisitScheduler> getVisitScheduler() {
        if (visitScheduler == null) {
            visitScheduler = new ArrayList<VisitScheduler>();
        }
        return this.visitScheduler;
    }

    /**
     * Gets the value of the caseProgress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the caseProgress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCaseProgress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CaseProgress }
     * 
     * 
     */
    public List<CaseProgress> getCaseProgress() {
        if (caseProgress == null) {
            caseProgress = new ArrayList<CaseProgress>();
        }
        return this.caseProgress;
    }

}
