package br.com.fiap3espg.autoescola.adapter.in.controller.response.instrutor;

import br.com.fiap3espg.autoescola.application.core.domain.Instrutor;
import br.com.fiap3espg.autoescola.common.vo.endereco.DadosEndereco;
import br.com.fiap3espg.autoescola.common.vo.enumeration.Especialidade;

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
