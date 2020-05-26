//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2019.05.20 ʱ�� 06:02:03 PM GMT+08:00 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.fileToRuiyang;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.filetoruiyangservice package. 
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

    private final static QName _FileToRuiyangRequest_QNAME = new QName("http://www.example.org/fileToRuiyangService", "fileToRuiyangRequest");
    private final static QName _FileToRuiyangResponse_QNAME = new QName("http://www.example.org/fileToRuiyangService", "fileToRuiyangResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.filetoruiyangservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FileToRuiyangRequest }
     * 
     */
    public FileToRuiyangRequest createFileToRuiyangRequest() {
        return new FileToRuiyangRequest();
    }

    /**
     * Create an instance of {@link FileToRuiyangResponse }
     * 
     */
    public FileToRuiyangResponse createFileToRuiyangResponse() {
        return new FileToRuiyangResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FileToRuiyangRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/fileToRuiyangService", name = "fileToRuiyangRequest")
    public JAXBElement<FileToRuiyangRequest> createFileToRuiyangRequest(FileToRuiyangRequest value) {
        return new JAXBElement<FileToRuiyangRequest>(_FileToRuiyangRequest_QNAME, FileToRuiyangRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FileToRuiyangResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/fileToRuiyangService", name = "fileToRuiyangResponse")
    public JAXBElement<FileToRuiyangResponse> createFileToRuiyangResponse(FileToRuiyangResponse value) {
        return new JAXBElement<FileToRuiyangResponse>(_FileToRuiyangResponse_QNAME, FileToRuiyangResponse.class, null, value);
    }

}
