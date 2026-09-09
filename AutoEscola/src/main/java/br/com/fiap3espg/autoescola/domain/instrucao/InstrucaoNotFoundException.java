package br.com.fiap3espg.autoescola.domain.instrucao;

public class InstrucaoNotFoundException extends RuntimeException {
    public InstrucaoNotFoundException(String message) {
        super(message);
    }
}
