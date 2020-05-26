//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.12.23 at 07:46:38 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.common;

import com.alibaba.fastjson.annotation.JSONField;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ServiceHeader complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ServiceHeader">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ChannelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReqTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ResTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ReqNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uuid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceHeader", propOrder = {
    "serviceType",
    "channelType",
    "reqTime",
    "resCode",
    "resMessage",
    "resTime",
    "reqNo",
    "token",
    "uuid"
})
public class ServiceHeader {

    @XmlElement(name = "ServiceType", required = true)
    @JSONField(name ="ServiceType",ordinal = 2)
    protected String serviceType;

    @XmlElement(name = "ChannelType", required = true)
    @JSONField(name ="ChannelType",ordinal = 3)
    protected String channelType;

    @JSONField(name ="ReqTime",ordinal = 4)
    @XmlElement(name = "ReqTime", required = true)
    protected String reqTime;

    @JSONField(name ="ResCode",ordinal = 5)
    @XmlElement(name = "ResCode", required = true)
    protected String resCode;

    @JSONField(name ="ResMessage",ordinal = 6)
    @XmlElement(name = "ResMessage", required = true)
    protected String resMessage;

    @JSONField(name ="ResTime",ordinal = 7)
    @XmlElement(name = "ResTime", required = true)
    protected String resTime;

    @JSONField(name ="ReqNo",ordinal = 8)
    @XmlElement(name = "ReqNo", required = true)
    protected String reqNo;

    @JSONField(name ="Token",ordinal = 9)
    @XmlElement(name = "Token", required = true)
    protected String token;

    @JSONField(name ="uuid",ordinal = 10)
    @XmlElement(name = "uuid", required = true)
    protected String uuid;

    @JSONField(name ="DeviceFingerprint",ordinal = 11)
    @XmlElement(name = "DeviceFingerprint", required = true)
    protected String deviceFingerprint;

    /**
     * ��ȡserviceType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * ����serviceType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
    }

    /**
     * ��ȡchannelType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelType() {
        return channelType;
    }

    /**
     * ����channelType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelType(String value) {
        this.channelType = value;
    }

    /**
     * ��ȡreqTime���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReqTime() {
        return reqTime;
    }

    /**
     * ����reqTime���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReqTime(String value) {
        this.reqTime = value;
    }

    /**
     * ��ȡresCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResCode() {
        return resCode;
    }

    /**
     * ����resCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResCode(String value) {
        this.resCode = value;
    }

    /**
     * ��ȡresMessage���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResMessage() {
        return resMessage;
    }

    /**
     * ����resMessage���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResMessage(String value) {
        this.resMessage = value;
    }

    /**
     * ��ȡresTime���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResTime() {
        return resTime;
    }

    /**
     * ����resTime���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResTime(String value) {
        this.resTime = value;
    }

    /**
     * ��ȡreqNo���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReqNo() {
        return reqNo;
    }

    /**
     * ����reqNo���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReqNo(String value) {
        this.reqNo = value;
    }

    /**
     * ��ȡtoken���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * ����token���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

    /**
     * ��ȡuuid���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * ����uuid���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUuid(String value) {
        this.uuid = value;
    }


    public String getDeviceFingerprint() {
        return deviceFingerprint;
    }

    public void setDeviceFingerprint(String deviceFingerprint) {
        this.deviceFingerprint = deviceFingerprint;
    }

}