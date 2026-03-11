package br.com.fiap3espg;

import br.com.fiap3espg.service.Subtraction;
import br.com.fiap3espg.service.Sum;
import jakarta.xml.ws.Endpoint;

public class CalculatorApplication {
    public static void main(String[] args) {
        System.out.println("Publicando webservices...");
        Endpoint.publish("http://localhost:8080/calculadora/soma", new Sum());
        Endpoint.publish("http://localhost:8080/calculadora/subtracao", new Subtraction());
        System.out.println("Webservices publicados com sucesso!");
    }
}
