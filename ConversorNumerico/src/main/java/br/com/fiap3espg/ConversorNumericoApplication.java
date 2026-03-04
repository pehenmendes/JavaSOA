package br.com.fiap3espg;

import br.com.fiap3espg.stubclasses.NumberConversionSoapType;
import jakarta.xml.ws.Service;

import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;

public class ConversorNumericoApplication {
    public static void main(String[] args) throws MalformedURLException {
        final String WSDL = "https://www.dataaccess.com/webservicesserver/NumberConversion.wso?WSDL";
        final String TARGET = "http://www.dataaccess.com/webservicesserver/";
        final String NAME = "NumberConversion";

        URL url = new URL(WSDL);
        QName qName = new QName(TARGET, NAME);
        Service service = Service.create(url, qName);

        NumberConversionSoapType numConversion = service.getPort(NumberConversionSoapType.class);
        String convertedNumber = numConversion.numberToWords(BigInteger.valueOf(182475));
        System.out.println(convertedNumber);
    }
}
