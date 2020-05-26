//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.08.07 时间 05:51:31 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getLogFileList;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.getlogfilelist package. 
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

    private final static QName _GetLogFileListRs_QNAME = new QName("http://www.example.org/getLogFileList", "getLogFileListRs");
    private final static QName _GetLogFileListRq_QNAME = new QName("http://www.example.org/getLogFileList", "getLogFileListRq");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.getlogfilelist
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetLogFileListRs }
     * 
     */
    public GetLogFileListRs createLogFileList() {
        return new GetLogFileListRs();
    }

    /**
     * Create an instance of {@link GetLogFileListRq }
     * 
     */
    public GetLogFileListRq createGetLogFileListRq() {
        return new GetLogFileListRq();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link LogFile }
     * 
     */
    public LogFile createLogFile() {
        return new LogFile();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogFileListRs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getLogFileList", name = "getLogFileListRs")
    public JAXBElement<GetLogFileListRs> createGetLogFileListRs(GetLogFileListRs value) {
        return new JAXBElement<GetLogFileListRs>(_GetLogFileListRs_QNAME, GetLogFileListRs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLogFileListRq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getLogFileList", name = "getLogFileListRq")
    public JAXBElement<GetLogFileListRq> createGetLogFileListRq(GetLogFileListRq value) {
        return new JAXBElement<GetLogFileListRq>(_GetLogFileListRq_QNAME, GetLogFileListRq.class, null, value);
    }

}
