//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.05.29 时间 11:44:52 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>webSendMessageRq complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="webSendMessageRq">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="messageType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="messageTitle" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="messageContext" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sendUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="acceptUser" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="isPush" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "webSendMessageRq", propOrder = {
    "messageType",
    "messageTitle",
    "messageContext",
    "sendUser",
    "acceptUser",
    "isPush"
})
public class WebSendMessageRq {

    @XmlElement(required = true)
    protected String messageType;
    @XmlElement(required = true)
    protected String messageTitle;
    @XmlElement(required = true)
    protected String messageContext;
    @XmlElement(required = true)
    protected String sendUser;
    @XmlElement(required = true)
    protected String acceptUser;
    @XmlElement(required = true)
    protected String isPush;

    /**
     * 获取messageType属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * 设置messageType属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageType(String value) {
        this.messageType = value;
    }

    /**
     * 获取messageTitle属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageTitle() {
        return messageTitle;
    }

    /**
     * 设置messageTitle属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageTitle(String value) {
        this.messageTitle = value;
    }

    /**
     * 获取messageContext属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageContext() {
        return messageContext;
    }

    /**
     * 设置messageContext属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageContext(String value) {
        this.messageContext = value;
    }

    /**
     * 获取sendUser属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSendUser() {
        return sendUser;
    }

    /**
     * 设置sendUser属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendUser(String value) {
        this.sendUser = value;
    }

    /**
     * 获取acceptUser属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcceptUser() {
        return acceptUser;
    }

    /**
     * 设置acceptUser属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcceptUser(String value) {
        this.acceptUser = value;
    }

    /**
     * 获取isPush属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsPush() {
        return isPush;
    }

    /**
     * 设置isPush属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsPush(String value) {
        this.isPush = value;
    }

}
