package br.com.fiap3espg.autoescola.domain.aluno;

import br.com.fiap3espg.autoescola.domain.endereco.DadosEndereco;

public record DadosDetalhamentoAluno(
        Long id,
        String nome,
        String email,
        String telefone,
        String cpf,
        String matricula,
        String tipoCarta,
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
