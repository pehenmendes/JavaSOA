package br.com.fiap3espg.autoescola.domain.instrutor;

import br.com.fiap3espg.autoescola.domain.endereco.DadosEndereco;

public record DadosDetalhamentoInstrutor(
        Long id,
        String nome,
        String telefone,
        String cnh,
        Especialidade especialidade,
        DadosEndereco endereco,
        boolean ativo
) {
    public DadosDetalhamentoInstrutor(Instrutor instrutor) {
        this(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                instrutor.getCnh(),
                instrutor.getEspecialidade(),
                new DadosEndereco(instrutor.getEndereco()),
                instrutor.isAtivo());
    }
}
