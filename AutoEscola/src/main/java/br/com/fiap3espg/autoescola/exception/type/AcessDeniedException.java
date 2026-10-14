package br.com.fiap3espg.autoescola.exception.type;

public class AcessDeniedException extends RuntimeException {
    public AcessDeniedException(String message) {
        super(message);
    }
}
