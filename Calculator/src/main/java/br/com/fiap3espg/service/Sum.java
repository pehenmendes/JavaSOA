package br.com.fiap3espg.service;

import jakarta.jws.WebService;

@WebService(endpointInterface = "br.com.fiap3espg.service.Operation")
public class Sum implements Operation {

    @Override
    public String showName() {
        return "SOMA";
    }

    @Override
    public String showOperator() {
        return "+";
    }

    @Override
    public double calculate(double a, double b) {
        return a + b;
    }

    @Override
    public String showResult(double a, double b, double r) {
        return "O resultado da " + showName() + " de (" + a + " " + showOperator() + " " + b + ") É igual a " + r + ".";
    }
}
