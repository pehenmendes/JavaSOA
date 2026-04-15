package br.com.fiap3espg.autoescola.instrutor;

import br.com.fiap3espg.autoescola.endereco.DadosEndereco;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoInstrutor(
        @NotNull
        Long id,

        String nome,
        String email,
        Especialidade especialidade,
        DadosEndereco endereco) {
}
