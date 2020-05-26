//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.03.22 时间 02:37:11 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getManagerHoneInfo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.getmanagerhomeinfo package. 
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

    private final static QName _ReachingRateList_QNAME = new QName("http://www.example.org/getManagerHomeInfo", "reachingRateList");
    private final static QName _RateOfDay_QNAME = new QName("http://www.example.org/getManagerHomeInfo", "rateOfDay");
    private final static QName _GetManagerHomeInfoRs_QNAME = new QName("http://www.example.org/getManagerHomeInfo", "getManagerHomeInfoRs");
    private final static QName _ProdList_QNAME = new QName("http://www.example.org/getManagerHomeInfo", "prodList");
    private final static QName _GetManagerHomeInfoRq_QNAME = new QName("http://www.example.org/getManagerHomeInfo", "getManagerHomeInfoRq");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.getmanagerhomeinfo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ReachingRateList }
     * 
     */
    public ReachingRateList createReachingRateList() {
        return new ReachingRateList();
    }

    /**
     * Create an instance of {@link GetManagerHomeInfoRs }
     * 
     */
    public GetManagerHomeInfoRs createGetManagerHomeInfoRs() {
        return new GetManagerHomeInfoRs();
    }

    /**
     * Create an instance of {@link List }
     * 
     */
    public List createList() {
        return new List();
    }

    /**
     * Create an instance of {@link GetManagerHomeInfoRq }
     * 
     */
    public GetManagerHomeInfoRq createGetManagerHomeInfoRq() {
        return new GetManagerHomeInfoRq();
    }

    /**
     * Create an instance of {@link ReachingRateInfo }
     * 
     */
    public ReachingRateInfo createReachingRateInfo() {
        return new ReachingRateInfo();
    }

    /**
     * Create an instance of {@link ProdList }
     * 
     */
    public ProdList createProdList() {
        return new ProdList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReachingRateList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getManagerHomeInfo", name = "reachingRateList")
    public JAXBElement<ReachingRateList> createReachingRateList(ReachingRateList value) {
        return new JAXBElement<ReachingRateList>(_ReachingRateList_QNAME, ReachingRateList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getManagerHomeInfo", name = "rateOfDay")
    public JAXBElement<Double> createRateOfDay(Double value) {
        return new JAXBElement<Double>(_RateOfDay_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetManagerHomeInfoRs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getManagerHomeInfo", name = "getManagerHomeInfoRs")
    public JAXBElement<GetManagerHomeInfoRs> createGetManagerHomeInfoRs(GetManagerHomeInfoRs value) {
        return new JAXBElement<GetManagerHomeInfoRs>(_GetManagerHomeInfoRs_QNAME, GetManagerHomeInfoRs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getManagerHomeInfo", name = "prodList")
    public JAXBElement<List> createProdList(List value) {
        return new JAXBElement<List>(_ProdList_QNAME, List.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetManagerHomeInfoRq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getManagerHomeInfo", name = "getManagerHomeInfoRq")
    public JAXBElement<GetManagerHomeInfoRq> createGetManagerHomeInfoRq(GetManagerHomeInfoRq value) {
        return new JAXBElement<GetManagerHomeInfoRq>(_GetManagerHomeInfoRq_QNAME, GetManagerHomeInfoRq.class, null, value);
    }

}
