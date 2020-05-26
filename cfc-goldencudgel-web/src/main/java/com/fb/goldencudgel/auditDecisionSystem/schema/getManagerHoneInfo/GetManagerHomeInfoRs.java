//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.03.22 时间 02:37:11 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>getManagerHomeInfoRs complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="getManagerHomeInfoRs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="workListEnable" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reachingRateInfo" type="{http://www.example.org/getManagerHomeInfo}reachingRateInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getManagerHomeInfoRs", propOrder = {
    "workListEnable",
    "reachingRateInfo"
})
public class GetManagerHomeInfoRs {

    @XmlElement(required = true)
    protected String workListEnable;
    protected List<ReachingRateInfo> reachingRateInfo;

    /**
     * 获取workListEnable属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkListEnable() {
        return workListEnable;
    }

    /**
     * 设置workListEnable属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkListEnable(String value) {
        this.workListEnable = value;
    }

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

}
