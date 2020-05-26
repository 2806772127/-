//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.8-b130911.1802 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.08.05 时间 05:24:06 PM CST 
//


package com.fb.goldencudgel.auditDecisionSystem.schema.getReportData;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.example.getreportdata package. 
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

    private final static QName _GetReportDataRs_QNAME = new QName("http://www.example.org/getReportData", "getReportDataRs");
    private final static QName _GetReportDataRq_QNAME = new QName("http://www.example.org/getReportData", "getReportDataRq");
    private final static QName _Columns_QNAME = new QName("http://www.example.org/getReportData", "columns");
    private final static QName _FilePath_QNAME = new QName("http://www.example.org/getReportData", "filePath");
    private final static QName _DataHeader_QNAME = new QName("http://www.example.org/getReportData", "dataHeader");
    private final static QName _ResultSet_QNAME = new QName("http://www.example.org/getReportData", "resultSet");
    private final static QName _QueryStr_QNAME = new QName("http://www.example.org/getReportData", "queryStr");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.example.getreportdata
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetReportDataRs }
     * 
     */
    public GetReportDataRs createGetReportDataRs() {
        return new GetReportDataRs();
    }

    /**
     * Create an instance of {@link GetReportDataRq }
     * 
     */
    public GetReportDataRq createGetReportDataRq() {
        return new GetReportDataRq();
    }

    /**
     * Create an instance of {@link Columns }
     * 
     */
    public Columns createColumns() {
        return new Columns();
    }

    /**
     * Create an instance of {@link ResultSet }
     * 
     */
    public ResultSet createResultSet() {
        return new ResultSet();
    }

    /**
     * Create an instance of {@link Column }
     * 
     */
    public Column createColumn() {
        return new Column();
    }

    /**
     * Create an instance of {@link Rows }
     * 
     */
    public Rows createRows() {
        return new Rows();
    }

    /**
     * Create an instance of {@link ColumnName }
     * 
     */
    public ColumnName createColumnName() {
        return new ColumnName();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetReportDataRs }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getReportData", name = "getReportDataRs")
    public JAXBElement<GetReportDataRs> createGetReportDataRs(GetReportDataRs value) {
        return new JAXBElement<GetReportDataRs>(_GetReportDataRs_QNAME, GetReportDataRs.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetReportDataRq }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getReportData", name = "getReportDataRq")
    public JAXBElement<GetReportDataRq> createGetReportDataRq(GetReportDataRq value) {
        return new JAXBElement<GetReportDataRq>(_GetReportDataRq_QNAME, GetReportDataRq.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Columns }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getReportData", name = "columns")
    public JAXBElement<Columns> createColumns(Columns value) {
        return new JAXBElement<Columns>(_Columns_QNAME, Columns.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getReportData", name = "filePath")
    public JAXBElement<String> createFilePath(String value) {
        return new JAXBElement<String>(_FilePath_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getReportData", name = "dataHeader")
    public JAXBElement<String> createDataHeader(String value) {
        return new JAXBElement<String>(_DataHeader_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResultSet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getReportData", name = "resultSet")
    public JAXBElement<ResultSet> createResultSet(ResultSet value) {
        return new JAXBElement<ResultSet>(_ResultSet_QNAME, ResultSet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.example.org/getReportData", name = "queryStr")
    public JAXBElement<String> createQueryStr(String value) {
        return new JAXBElement<String>(_QueryStr_QNAME, String.class, null, value);
    }

}
