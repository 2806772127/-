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
 * <p>getSchedulerBaseInfoRq complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="getSchedulerBaseInfoRq">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="schedulerId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="uid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSchedulerBaseInfoRq", propOrder = {
        "schedulerId",
        "uid"
})
public class GetSchedulerBaseInfoRq {

    @XmlElement(required = true)
    protected String schedulerId;
    protected String uid;

    /**
     * 获取schedulerId属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSchedulerId() {
        return schedulerId;
    }

    /**
     * 设置schedulerId属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSchedulerId(String value) {
        this.schedulerId = value;
    }

    /**
     * 获取uid属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置uid属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUid(String value) {
        this.uid = value;
    }

}
