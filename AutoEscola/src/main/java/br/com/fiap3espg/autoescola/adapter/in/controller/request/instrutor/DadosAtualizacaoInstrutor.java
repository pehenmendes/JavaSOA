package br.com.fiap3espg.autoescola.adapter.in.controller.request.instrutor;

import br.com.fiap3espg.autoescola.common.vo.endereco.DadosEndereco;
import br.com.fiap3espg.autoescola.common.vo.enumeration.Especialidade;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoInstrutor(
        @NotNull
        Long id,

        String nome,
        String email,
        String telefone,
        Especialidade especialidade,
        DadosEndereco endereco) {
}
