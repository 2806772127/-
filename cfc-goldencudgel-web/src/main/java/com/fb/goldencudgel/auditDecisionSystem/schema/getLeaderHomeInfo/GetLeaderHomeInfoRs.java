//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.03.22 时间 02:56:23 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getLeaderHomeInfo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>getLeaderHomeInfoRs complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="getLeaderHomeInfoRs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="reachingRateInfo" type="{http://www.example.org/getLeaderHomeInfo}reachingRateInfo" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="visitScheduler" type="{http://www.example.org/getLeaderHomeInfo}visitScheduler" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="caseProgress" type="{http://www.example.org/getLeaderHomeInfo}caseProgress" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getLeaderHomeInfoRs", propOrder = {
    "reachingRateInfo",
    "visitScheduler",
    "caseProgress"
})
public class GetLeaderHomeInfoRs {

    protected List<ReachingRateInfo> reachingRateInfo;
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
