package br.com.fiap3espg.autoescola.domain.usuario;

public class SenhaAtualIncorretaException extends RuntimeException {
    public SenhaAtualIncorretaException(String message) {
        super(message);
    }
}
