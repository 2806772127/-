package com.fb.goldencudgel.auditDecisionSystem.schema.callZyjService;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.callzyjservice package. 
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

    private final static QName _ZYJRs_QNAME = new QName("http://www.example.org/callZYJService", "ZYJRs");
    private final static QName _ZYJRq_QNAME = new QName("http://www.example.org/callZYJService", "ZYJRq");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.callzyjservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ZYJRs }
     * 
     */
    public ZYJRs createZYJRs() {
        return new ZYJRs();
    }

    /**
     * Create an instance of {@link ZYJRq }
     * 
     */
    public ZYJRq createZYJRq() {
        return new ZYJRq();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ZYJRs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/callZYJService", name = "ZYJRs")
    public JAXBElement<ZYJRs> createZYJRs(ZYJRs value) {
        return new JAXBElement<ZYJRs>(_ZYJRs_QNAME, ZYJRs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ZYJRq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/callZYJService", name = "ZYJRq")
    public JAXBElement<ZYJRq> createZYJRq(ZYJRq value) {
        return new JAXBElement<ZYJRq>(_ZYJRq_QNAME, ZYJRq.class, null, value);
    }

}
