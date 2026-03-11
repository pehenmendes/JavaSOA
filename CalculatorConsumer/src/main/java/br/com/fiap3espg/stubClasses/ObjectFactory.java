
package br.com.fiap3espg.stubClasses;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the br.com.fiap3espg.stubClasses package. 
 * <p>An ObjectFactory allows you to programmatically 
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

    private static final QName _Calculate_QNAME = new QName("http://service.fiap3espg.com.br/", "calculate");
    private static final QName _CalculateResponse_QNAME = new QName("http://service.fiap3espg.com.br/", "calculateResponse");
    private static final QName _ShowName_QNAME = new QName("http://service.fiap3espg.com.br/", "showName");
    private static final QName _ShowNameResponse_QNAME = new QName("http://service.fiap3espg.com.br/", "showNameResponse");
    private static final QName _ShowOperator_QNAME = new QName("http://service.fiap3espg.com.br/", "showOperator");
    private static final QName _ShowOperatorResponse_QNAME = new QName("http://service.fiap3espg.com.br/", "showOperatorResponse");
    private static final QName _ShowResult_QNAME = new QName("http://service.fiap3espg.com.br/", "showResult");
    private static final QName _ShowResultResponse_QNAME = new QName("http://service.fiap3espg.com.br/", "showResultResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: br.com.fiap3espg.stubClasses
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Calculate }
     * 
     * @return
     *     the new instance of {@link Calculate }
     */
    public Calculate createCalculate() {
        return new Calculate();
    }

    /**
     * Create an instance of {@link CalculateResponse }
     * 
     * @return
     *     the new instance of {@link CalculateResponse }
     */
    public CalculateResponse createCalculateResponse() {
        return new CalculateResponse();
    }

    /**
     * Create an instance of {@link ShowName }
     * 
     * @return
     *     the new instance of {@link ShowName }
     */
    public ShowName createShowName() {
        return new ShowName();
    }

    /**
     * Create an instance of {@link ShowNameResponse }
     * 
     * @return
     *     the new instance of {@link ShowNameResponse }
     */
    public ShowNameResponse createShowNameResponse() {
        return new ShowNameResponse();
    }

    /**
     * Create an instance of {@link ShowOperator }
     * 
     * @return
     *     the new instance of {@link ShowOperator }
     */
    public ShowOperator createShowOperator() {
        return new ShowOperator();
    }

    /**
     * Create an instance of {@link ShowOperatorResponse }
     * 
     * @return
     *     the new instance of {@link ShowOperatorResponse }
     */
    public ShowOperatorResponse createShowOperatorResponse() {
        return new ShowOperatorResponse();
    }

    /**
     * Create an instance of {@link ShowResult }
     * 
     * @return
     *     the new instance of {@link ShowResult }
     */
    public ShowResult createShowResult() {
        return new ShowResult();
    }

    /**
     * Create an instance of {@link ShowResultResponse }
     * 
     * @return
     *     the new instance of {@link ShowResultResponse }
     */
    public ShowResultResponse createShowResultResponse() {
        return new ShowResultResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Calculate }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Calculate }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.fiap3espg.com.br/", name = "calculate")
    public JAXBElement<Calculate> createCalculate(Calculate value) {
        return new JAXBElement<>(_Calculate_QNAME, Calculate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CalculateResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CalculateResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.fiap3espg.com.br/", name = "calculateResponse")
    public JAXBElement<CalculateResponse> createCalculateResponse(CalculateResponse value) {
        return new JAXBElement<>(_CalculateResponse_QNAME, CalculateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShowName }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ShowName }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.fiap3espg.com.br/", name = "showName")
    public JAXBElement<ShowName> createShowName(ShowName value) {
        return new JAXBElement<>(_ShowName_QNAME, ShowName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShowNameResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ShowNameResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.fiap3espg.com.br/", name = "showNameResponse")
    public JAXBElement<ShowNameResponse> createShowNameResponse(ShowNameResponse value) {
        return new JAXBElement<>(_ShowNameResponse_QNAME, ShowNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShowOperator }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ShowOperator }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.fiap3espg.com.br/", name = "showOperator")
    public JAXBElement<ShowOperator> createShowOperator(ShowOperator value) {
        return new JAXBElement<>(_ShowOperator_QNAME, ShowOperator.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShowOperatorResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ShowOperatorResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.fiap3espg.com.br/", name = "showOperatorResponse")
    public JAXBElement<ShowOperatorResponse> createShowOperatorResponse(ShowOperatorResponse value) {
        return new JAXBElement<>(_ShowOperatorResponse_QNAME, ShowOperatorResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShowResult }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ShowResult }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.fiap3espg.com.br/", name = "showResult")
    public JAXBElement<ShowResult> createShowResult(ShowResult value) {
        return new JAXBElement<>(_ShowResult_QNAME, ShowResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShowResultResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ShowResultResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://service.fiap3espg.com.br/", name = "showResultResponse")
    public JAXBElement<ShowResultResponse> createShowResultResponse(ShowResultResponse value) {
        return new JAXBElement<>(_ShowResultResponse_QNAME, ShowResultResponse.class, null, value);
    }

}
