//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.05.20 时间 06:02:03 PM GMT+08:00
//


package com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>fileToRuiyangResponse complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="fileToRuiyangResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EMSGID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EMSGTXT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fileToRuiyangResponse", propOrder = {
        "emsgid",
        "emsgtxt"
})
public class FileToRuiyangResponse {

    @XmlElement(name = "emsgid")
    protected String emsgid;
    @XmlElement(name = "emsgtxt")
    protected String emsgtxt;

    /**
     * 获取emsgid属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEMSGID() {
        return emsgid;
    }

    /**
     * 设置emsgid属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEMSGID(String value) {
        this.emsgid = value;
    }

    /**
     * 获取emsgtxt属性的值。
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEMSGTXT() {
        return emsgtxt;
    }

    /**
     * 设置emsgtxt属性的值。
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEMSGTXT(String value) {
        this.emsgtxt = value;
    }

}
