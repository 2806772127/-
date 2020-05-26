//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.04.02 时间 03:38:42 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getUserProfile;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.getuserprofile package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetUserProfileRs_QNAME = new QName("http://www.example.org/getUserProfile", "getUserProfileRs");
    private final static QName _GetUserProfileRq_QNAME = new QName("http://www.example.org/getUserProfile", "getUserProfileRq");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.getuserprofile
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetUserProfileRq }
     * 
     */
    public GetUserProfileRq createGetUserProfileRq() {
        return new GetUserProfileRq();
    }

    /**
     * Create an instance of {@link GetUserProfileRs }
     * 
     */
    public GetUserProfileRs createGetUserProfileRs() {
        return new GetUserProfileRs();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserProfileRs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getUserProfile", name = "getUserProfileRs")
    public JAXBElement<GetUserProfileRs> createGetUserProfileRs(GetUserProfileRs value) {
        return new JAXBElement<GetUserProfileRs>(_GetUserProfileRs_QNAME, GetUserProfileRs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserProfileRq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getUserProfile", name = "getUserProfileRq")
    public JAXBElement<GetUserProfileRq> createGetUserProfileRq(GetUserProfileRq value) {
        return new JAXBElement<GetUserProfileRq>(_GetUserProfileRq_QNAME, GetUserProfileRq.class, null, value);
    }

}
