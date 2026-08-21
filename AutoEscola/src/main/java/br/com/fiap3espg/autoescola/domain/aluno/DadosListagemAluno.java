package br.com.fiap3espg.autoescola.domain.aluno;

import br.com.fiap3espg.autoescola.domain.endereco.DadosEndereco;

public record DadosListagemAluno(
        Long id,
        String nome,
        String email,
        String matricula,
        String tipoCarta) {
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
