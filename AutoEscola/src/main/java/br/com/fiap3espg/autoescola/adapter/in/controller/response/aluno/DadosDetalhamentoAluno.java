package br.com.fiap3espg.autoescola.adapter.in.controller.response.aluno;

import br.com.fiap3espg.autoescola.application.core.domain.Aluno;
import br.com.fiap3espg.autoescola.common.vo.endereco.DadosEndereco;
import br.com.fiap3espg.autoescola.common.vo.enumeration.TipoCarta;

public record DadosDetalhamentoAluno(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        String matricula,
        TipoCarta tipoCarta,
        DadosEndereco endereco,
        boolean ativo
) {
    public DadosDetalhamentoAluno(Aluno aluno) {
        this(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getCpf(),
                aluno.getMatricula(),
                aluno.getTipoCarta(),
                new DadosEndereco(aluno.getEndereco()),
                aluno.isAtivo()
        );
    }
}
