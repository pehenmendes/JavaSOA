package br.com.fiap3espg.autoescola.domain.aluno;

public record DadosListagemAluno(
        Long id,
        String nome,
        String email,
        String matricula,
        TipoCarta tipoCarta) {
    public DadosListagemAluno(Aluno aluno) {
        this(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getMatricula(),
                aluno.getTipoCarta()
        );
    }
}
