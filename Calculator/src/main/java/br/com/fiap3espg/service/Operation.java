package br.com.fiap3espg.service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.DOCUMENT)
public interface Operation {
    @WebMethod
    String showName();

    @WebMethod
    String showOperator();

    @WebMethod
    double calculate(double a, double b);

    @WebMethod
    String showResult(double a, double b, double r);
}
