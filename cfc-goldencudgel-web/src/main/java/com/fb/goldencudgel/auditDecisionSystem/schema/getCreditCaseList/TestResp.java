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
 * <p>testResp complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="testResp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="caseWebServiceResp" type="{http://www.example.org/getCreditCaseList}caseWebServiceResp"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "testResp", propOrder = {
    "caseWebServiceResp"
})
public class TestResp {

    @XmlElement(required = true)
    protected CaseWebServiceResp caseWebServiceResp;

    /**
     * 获取caseWebServiceResp属性的值。
     * 
     * @return
     *     possible object is
     *     {@link CaseWebServiceResp }
     *     
     */
    public CaseWebServiceResp getCaseWebServiceResp() {
        return caseWebServiceResp;
    }

    /**
     * 设置caseWebServiceResp属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link CaseWebServiceResp }
     *     
     */
    public void setCaseWebServiceResp(CaseWebServiceResp value) {
        this.caseWebServiceResp = value;
    }

}
