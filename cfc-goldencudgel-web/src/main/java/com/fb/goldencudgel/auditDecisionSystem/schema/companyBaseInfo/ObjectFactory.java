//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.09.09 时间 11:11:35 AM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.companyBaseInfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.getcompanybaseinfo package. 
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

    private final static QName _GetCompanyBaseInfoRq_QNAME = new QName("http://www.example.org/getCompanyBaseInfo", "getCompanyBaseInfoRq");
    private final static QName _GetCompanyBaseInfoRs_QNAME = new QName("http://www.example.org/getCompanyBaseInfo", "getCompanyBaseInfoRs");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.getcompanybaseinfo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetCompanyBaseInfoRq }
     * 
     */
    public GetCompanyBaseInfoRq createGetCompanyBaseInfoRq() {
        return new GetCompanyBaseInfoRq();
    }

    /**
     * Create an instance of {@link GetCompanyBaseInfoRs }
     * 
     */
    public GetCompanyBaseInfoRs createGetCompanyBaseInfoRs() {
        return new GetCompanyBaseInfoRs();
    }

    /**
     * Create an instance of {@link TransList }
     * 
     */
    public TransList createTransList() {
        return new TransList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCompanyBaseInfoRq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getCompanyBaseInfo", name = "getCompanyBaseInfoRq")
    public JAXBElement<GetCompanyBaseInfoRq> createGetCompanyBaseInfoRq(GetCompanyBaseInfoRq value) {
        return new JAXBElement<GetCompanyBaseInfoRq>(_GetCompanyBaseInfoRq_QNAME, GetCompanyBaseInfoRq.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCompanyBaseInfoRs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getCompanyBaseInfo", name = "getCompanyBaseInfoRs")
    public JAXBElement<GetCompanyBaseInfoRs> createGetCompanyBaseInfoRs(GetCompanyBaseInfoRs value) {
        return new JAXBElement<GetCompanyBaseInfoRs>(_GetCompanyBaseInfoRs_QNAME, GetCompanyBaseInfoRs.class, null, value);
    }

}
