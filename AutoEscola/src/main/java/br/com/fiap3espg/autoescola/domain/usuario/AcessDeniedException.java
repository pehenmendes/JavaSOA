package br.com.fiap3espg.autoescola.domain.usuario;

public class AcessDeniedException extends RuntimeException {
    public AcessDeniedException(String message) {
        super(message);
    }
}
