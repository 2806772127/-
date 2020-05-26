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
 * <p>fileToRuiyangRequest complex type的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType name="fileToRuiyangRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="APPOINTMENT_ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ZJ_CREDIT_NUM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="COMPILATION_NO" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fileToRuiyangRequest", propOrder = {
        "appointmentid",
        "zjcreditnum",
        "compilationno"
})
public class FileToRuiyangRequest {

    @XmlElement(name = "appointmentid")
    protected String appointmentid;
    @XmlElement(name = "zjcreditnum")
    protected String zjcreditnum;
    @XmlElement(name = "compilationno")
    protected String compilationno;

    public String getAppointmentid() {
        return appointmentid;
    }

    public void setAppointmentid(String appointmentid) {
        this.appointmentid = appointmentid;
    }

    public String getZjcreditnum() {
        return zjcreditnum;
    }

    public void setZjcreditnum(String zjcreditnum) {
        this.zjcreditnum = zjcreditnum;
    }

    public String getCompilationno() {
        return compilationno;
    }

    public void setCompilationno(String compilationno) {
        this.compilationno = compilationno;
    }
}
