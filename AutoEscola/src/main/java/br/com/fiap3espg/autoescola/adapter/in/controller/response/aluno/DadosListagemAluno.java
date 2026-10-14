package br.com.fiap3espg.autoescola.adapter.in.controller.response.aluno;

import br.com.fiap3espg.autoescola.application.core.domain.Aluno;
import br.com.fiap3espg.autoescola.common.vo.enumeration.TipoCarta;

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
