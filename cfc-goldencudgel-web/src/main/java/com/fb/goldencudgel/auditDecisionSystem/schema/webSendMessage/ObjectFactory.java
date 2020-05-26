//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.05.29 时间 11:44:52 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.webSendMessage;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.websendmessage package. 
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

    private final static QName _WebSendMessageRs_QNAME = new QName("http://www.example.org/webSendMessage", "webSendMessageRs");
    private final static QName _AcceptCodes_QNAME = new QName("http://www.example.org/webSendMessage", "acceptCodes");
    private final static QName _WebSendMessageRq_QNAME = new QName("http://www.example.org/webSendMessage", "webSendMessageRq");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.websendmessage
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Code }
     * 
     */
    public Code createCode() {
        return new Code();
    }

    /**
     * Create an instance of {@link WebSendMessageRq }
     * 
     */
    public WebSendMessageRq createWebSendMessageRq() {
        return new WebSendMessageRq();
    }

    /**
     * Create an instance of {@link WebSendMessageRs }
     * 
     */
    public WebSendMessageRs createWebSendMessageRs() {
        return new WebSendMessageRs();
    }

    /**
     * Create an instance of {@link AcceptCodes }
     * 
     */
    public AcceptCodes createAcceptCodes() {
        return new AcceptCodes();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WebSendMessageRs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/webSendMessage", name = "webSendMessageRs")
    public JAXBElement<WebSendMessageRs> createWebSendMessageRs(WebSendMessageRs value) {
        return new JAXBElement<WebSendMessageRs>(_WebSendMessageRs_QNAME, WebSendMessageRs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Code }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/webSendMessage", name = "acceptCodes")
    public JAXBElement<Code> createAcceptCodes(Code value) {
        return new JAXBElement<Code>(_AcceptCodes_QNAME, Code.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WebSendMessageRq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/webSendMessage", name = "webSendMessageRq")
    public JAXBElement<WebSendMessageRq> createWebSendMessageRq(WebSendMessageRq value) {
        return new JAXBElement<WebSendMessageRq>(_WebSendMessageRq_QNAME, WebSendMessageRq.class, null, value);
    }

}
