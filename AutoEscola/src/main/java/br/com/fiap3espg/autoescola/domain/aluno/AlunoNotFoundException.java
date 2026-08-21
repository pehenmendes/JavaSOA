package br.com.fiap3espg.autoescola.domain.aluno;

public class AlunoNotFoundException extends RuntimeException {
    public AlunoNotFoundException(String message) {
        super(message);
    }
}