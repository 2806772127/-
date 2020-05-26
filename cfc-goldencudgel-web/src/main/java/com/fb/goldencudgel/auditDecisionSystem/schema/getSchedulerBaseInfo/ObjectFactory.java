//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.08.22 时间 11:37:23 AM CST
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getSchedulerBaseInfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.getschedulerbaseinfo package. 
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

    private final static QName _CaseNo_QNAME = new QName("http://www.example.org/getSchedulerBaseInfo", "caseNo");
    private final static QName _GetSchedulerBaseInfoRq_QNAME = new QName("http://www.example.org/getSchedulerBaseInfo", "getSchedulerBaseInfoRq");
    private final static QName _XMLschema_QNAME = new QName("http://www.example.org/getSchedulerBaseInfo", "XMLschema");
    private final static QName _GetSchedulerBaseInfoRs_QNAME = new QName("http://www.example.org/getSchedulerBaseInfo", "getSchedulerBaseInfoRs");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.getschedulerbaseinfo
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetSchedulerBaseInfoRq }
     *
     */
    public GetSchedulerBaseInfoRq createGetSchedulerBaseInfoRq() {
        return new GetSchedulerBaseInfoRq();
    }

    /**
     * Create an instance of {@link XMLschema }
     *
     */
    public XMLschema createXMLschema() {
        return new XMLschema();
    }

    /**
     * Create an instance of {@link GetSchedulerBaseInfoRs }
     *
     */
    public GetSchedulerBaseInfoRs createGetSchedulerBaseInfoRs() {
        return new GetSchedulerBaseInfoRs();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.example.org/getSchedulerBaseInfo", name = "caseNo")
    public JAXBElement<String> createCaseNo(String value) {
        return new JAXBElement<String>(_CaseNo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSchedulerBaseInfoRq }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.example.org/getSchedulerBaseInfo", name = "getSchedulerBaseInfoRq")
    public JAXBElement<GetSchedulerBaseInfoRq> createGetSchedulerBaseInfoRq(GetSchedulerBaseInfoRq value) {
        return new JAXBElement<GetSchedulerBaseInfoRq>(_GetSchedulerBaseInfoRq_QNAME, GetSchedulerBaseInfoRq.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLschema }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.example.org/getSchedulerBaseInfo", name = "XMLschema")
    public JAXBElement<XMLschema> createXMLschema(XMLschema value) {
        return new JAXBElement<XMLschema>(_XMLschema_QNAME, XMLschema.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSchedulerBaseInfoRs }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.example.org/getSchedulerBaseInfo", name = "getSchedulerBaseInfoRs")
    public JAXBElement<GetSchedulerBaseInfoRs> createGetSchedulerBaseInfoRs(GetSchedulerBaseInfoRs value) {
        return new JAXBElement<GetSchedulerBaseInfoRs>(_GetSchedulerBaseInfoRs_QNAME, GetSchedulerBaseInfoRs.class, null, value);
    }

}
