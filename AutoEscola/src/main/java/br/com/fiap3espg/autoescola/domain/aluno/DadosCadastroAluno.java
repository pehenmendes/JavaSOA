package br.com.fiap3espg.autoescola.domain.aluno;

import br.com.fiap3espg.autoescola.domain.endereco.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroAluno(
        @NotNull
        String nome,

        @NotNull
        String email,

        @NotNull
        String telefone,

        @NotNull
        @Pattern(regexp = "\\d{9,11}")
        String cpf,

        @NotNull
        @Pattern(regexp = "\\d{5}")
        String matricula,

        @NotNull
        String tipoCarta,

        @Valid
        DadosEndereco endereco) {
}
