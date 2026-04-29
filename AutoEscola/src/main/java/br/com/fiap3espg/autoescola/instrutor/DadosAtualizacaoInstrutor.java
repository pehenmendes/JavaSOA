package br.com.fiap3espg.autoescola.instrutor;

import br.com.fiap3espg.autoescola.endereco.DadosEndereco;
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
