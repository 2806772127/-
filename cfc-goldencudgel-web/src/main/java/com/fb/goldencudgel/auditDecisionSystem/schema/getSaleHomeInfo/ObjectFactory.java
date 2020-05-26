//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.03.22 时间 02:57:43 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getSaleHomeInfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.getsalehomeinfo package. 
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

    private final static QName _GetSaleHomeInfoRq_QNAME = new QName("http://www.example.org/getSaleHomeInfo", "getSaleHomeInfoRq");
    private final static QName _GetSaleHomeInfoRs_QNAME = new QName("http://www.example.org/getSaleHomeInfo", "getSaleHomeInfoRs");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.getsalehomeinfo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetSaleHomeInfoRq }
     * 
     */
    public GetSaleHomeInfoRq createGetSaleHomeInfoRq() {
        return new GetSaleHomeInfoRq();
    }

    /**
     * Create an instance of {@link GetSaleHomeInfoRs }
     * 
     */
    public GetSaleHomeInfoRs createGetSaleHomeInfoRs() {
        return new GetSaleHomeInfoRs();
    }

    /**
     * Create an instance of {@link ReachingRateInfo }
     * 
     */
    public ReachingRateInfo createReachingRateInfo() {
        return new ReachingRateInfo();
    }

    /**
     * Create an instance of {@link VisitScheduler }
     * 
     */
    public VisitScheduler createVisitScheduler() {
        return new VisitScheduler();
    }

    /**
     * Create an instance of {@link CaseProgress }
     * 
     */
    public CaseProgress createCaseProgress() {
        return new CaseProgress();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSaleHomeInfoRq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getSaleHomeInfo", name = "getSaleHomeInfoRq")
    public JAXBElement<GetSaleHomeInfoRq> createGetSaleHomeInfoRq(GetSaleHomeInfoRq value) {
        return new JAXBElement<GetSaleHomeInfoRq>(_GetSaleHomeInfoRq_QNAME, GetSaleHomeInfoRq.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSaleHomeInfoRs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getSaleHomeInfo", name = "getSaleHomeInfoRs")
    public JAXBElement<GetSaleHomeInfoRs> createGetSaleHomeInfoRs(GetSaleHomeInfoRs value) {
        return new JAXBElement<GetSaleHomeInfoRs>(_GetSaleHomeInfoRs_QNAME, GetSaleHomeInfoRs.class, null, value);
    }

}
