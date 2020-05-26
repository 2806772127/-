//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.03.29 时间 01:55:16 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getCreditCaseList;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.getcreditcaselist package. 
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

    private final static QName _GetCreditCaseListResp_QNAME = new QName("http://www.example.org/getCreditCaseList", "getCreditCaseListResp");
    private final static QName _CaseWebServiceReq_QNAME = new QName("http://www.example.org/getCreditCaseList", "caseWebServiceReq");
    private final static QName _WebRespString_QNAME = new QName("http://www.example.org/getCreditCaseList", "webRespString");
    private final static QName _CaseWebServiceResp_QNAME = new QName("http://www.example.org/getCreditCaseList", "caseWebServiceResp");
    private final static QName _SplitRecords_QNAME = new QName("http://www.example.org/getCreditCaseList", "splitRecords");
    private final static QName _GetCreditCaseListReq_QNAME = new QName("http://www.example.org/getCreditCaseList", "getCreditCaseListReq");
    private final static QName _CaseInfo_QNAME = new QName("http://www.example.org/getCreditCaseList", "caseInfo");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.getcreditcaselist
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CaseWebServiceResp }
     * 
     */
    public CaseWebServiceResp createCaseWebServiceResp() {
        return new CaseWebServiceResp();
    }

    /**
     * Create an instance of {@link SplitRecords }
     * 
     */
    public SplitRecords createSplitRecords() {
        return new SplitRecords();
    }

    /**
     * Create an instance of {@link CaseWebServiceReq }
     * 
     */
    public CaseWebServiceReq createCaseWebServiceReq() {
        return new CaseWebServiceReq();
    }

    /**
     * Create an instance of {@link CaseWebServiceRespString }
     * 
     */
    public CaseWebServiceRespString createCaseWebServiceRespString() {
        return new CaseWebServiceRespString();
    }

    /**
     * Create an instance of {@link GetCreditCaseListResp }
     * 
     */
    public GetCreditCaseListResp createGetCreditCaseListResp() {
        return new GetCreditCaseListResp();
    }

    /**
     * Create an instance of {@link CaseInfo }
     * 
     */
    public CaseInfo createCaseInfo() {
        return new CaseInfo();
    }

    /**
     * Create an instance of {@link GetCreditCaseListReq }
     * 
     */
    public GetCreditCaseListReq createGetCreditCaseListReq() {
        return new GetCreditCaseListReq();
    }

    /**
     * Create an instance of {@link RECORDS }
     * 
     */
    public RECORDS createRECORDS() {
        return new RECORDS();
    }

    /**
     * Create an instance of {@link TestResp }
     * 
     */
    public TestResp createTestResp() {
        return new TestResp();
    }

    /**
     * Create an instance of {@link RECORD }
     * 
     */
    public RECORD createRECORD() {
        return new RECORD();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCreditCaseListResp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getCreditCaseList", name = "getCreditCaseListResp")
    public JAXBElement<GetCreditCaseListResp> createGetCreditCaseListResp(GetCreditCaseListResp value) {
        return new JAXBElement<GetCreditCaseListResp>(_GetCreditCaseListResp_QNAME, GetCreditCaseListResp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CaseWebServiceReq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getCreditCaseList", name = "caseWebServiceReq")
    public JAXBElement<CaseWebServiceReq> createCaseWebServiceReq(CaseWebServiceReq value) {
        return new JAXBElement<CaseWebServiceReq>(_CaseWebServiceReq_QNAME, CaseWebServiceReq.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CaseWebServiceRespString }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getCreditCaseList", name = "webRespString")
    public JAXBElement<CaseWebServiceRespString> createWebRespString(CaseWebServiceRespString value) {
        return new JAXBElement<CaseWebServiceRespString>(_WebRespString_QNAME, CaseWebServiceRespString.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CaseWebServiceResp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getCreditCaseList", name = "caseWebServiceResp")
    public JAXBElement<CaseWebServiceResp> createCaseWebServiceResp(CaseWebServiceResp value) {
        return new JAXBElement<CaseWebServiceResp>(_CaseWebServiceResp_QNAME, CaseWebServiceResp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SplitRecords }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getCreditCaseList", name = "splitRecords")
    public JAXBElement<SplitRecords> createSplitRecords(SplitRecords value) {
        return new JAXBElement<SplitRecords>(_SplitRecords_QNAME, SplitRecords.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetCreditCaseListReq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getCreditCaseList", name = "getCreditCaseListReq")
    public JAXBElement<GetCreditCaseListReq> createGetCreditCaseListReq(GetCreditCaseListReq value) {
        return new JAXBElement<GetCreditCaseListReq>(_GetCreditCaseListReq_QNAME, GetCreditCaseListReq.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CaseInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getCreditCaseList", name = "caseInfo")
    public JAXBElement<CaseInfo> createCaseInfo(CaseInfo value) {
        return new JAXBElement<CaseInfo>(_CaseInfo_QNAME, CaseInfo.class, null, value);
    }

}
