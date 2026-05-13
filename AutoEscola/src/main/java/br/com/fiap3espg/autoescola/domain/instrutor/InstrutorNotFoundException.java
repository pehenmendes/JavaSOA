package br.com.fiap3espg.autoescola.domain.instrutor;

public class InstrutorNotFoundException extends RuntimeException {
    public InstrutorNotFoundException(String message) {
        super(message);
    }
}
