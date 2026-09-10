package br.com.fiap3espg.autoescola.domain.usuario;

public class SenhaIguaisException extends RuntimeException {
    public SenhaIguaisException(String message) {
        super(message);
    }
}
