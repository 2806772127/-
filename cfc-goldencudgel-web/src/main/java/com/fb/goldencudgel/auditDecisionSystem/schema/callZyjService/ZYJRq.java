package com.fb.goldencudgel.auditDecisionSystem.schema.callZyjService;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ZYJRq complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ZYJRq">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="compCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="compName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="trandId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mesaureId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="userCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZYJRq", propOrder = {
    "compCode",
    "compName",
    "trandId",
    "mesaureId",
    "userCode"
})
public class ZYJRq {

    @XmlElement(required = true)
    protected String compCode;
    @XmlElement(required = true)
    protected String compName;
    @XmlElement(required = true)
    protected String trandId;
    @XmlElement(required = true)
    protected String mesaureId;
    @XmlElement(required = true)
    protected String userCode;

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
     * 获取trandId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrandId() {
        return trandId;
    }

    /**
     * 设置trandId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrandId(String value) {
        this.trandId = value;
    }

    /**
     * 获取mesaureId属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMesaureId() {
        return mesaureId;
    }

    /**
     * 设置mesaureId属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMesaureId(String value) {
        this.mesaureId = value;
    }

    /**
     * 获取userCode属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * 设置userCode属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserCode(String value) {
        this.userCode = value;
    }

}
