//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.03.22 时间 02:56:23 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getLeaderHomeInfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.getleaderhomeinfo package. 
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

    private final static QName _GetLeaderHomeInfoRq_QNAME = new QName("http://www.example.org/getLeaderHomeInfo", "getLeaderHomeInfoRq");
    private final static QName _GetLeaderHomeInfoRs_QNAME = new QName("http://www.example.org/getLeaderHomeInfo", "getLeaderHomeInfoRs");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.getleaderhomeinfo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetLeaderHomeInfoRq }
     * 
     */
    public GetLeaderHomeInfoRq createGetLeaderHomeInfoRq() {
        return new GetLeaderHomeInfoRq();
    }

    /**
     * Create an instance of {@link GetLeaderHomeInfoRs }
     * 
     */
    public GetLeaderHomeInfoRs createGetLeaderHomeInfoRs() {
        return new GetLeaderHomeInfoRs();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLeaderHomeInfoRq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getLeaderHomeInfo", name = "getLeaderHomeInfoRq")
    public JAXBElement<GetLeaderHomeInfoRq> createGetLeaderHomeInfoRq(GetLeaderHomeInfoRq value) {
        return new JAXBElement<GetLeaderHomeInfoRq>(_GetLeaderHomeInfoRq_QNAME, GetLeaderHomeInfoRq.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLeaderHomeInfoRs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getLeaderHomeInfo", name = "getLeaderHomeInfoRs")
    public JAXBElement<GetLeaderHomeInfoRs> createGetLeaderHomeInfoRs(GetLeaderHomeInfoRs value) {
        return new JAXBElement<GetLeaderHomeInfoRs>(_GetLeaderHomeInfoRs_QNAME, GetLeaderHomeInfoRs.class, null, value);
    }

}
