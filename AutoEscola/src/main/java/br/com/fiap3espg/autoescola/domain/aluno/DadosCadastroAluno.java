package br.com.fiap3espg.autoescola.domain.aluno;

import br.com.fiap3espg.autoescola.domain.endereco.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroAluno(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String telefone,

        @NotBlank
        @Pattern(regexp = "\\d{11}")
        String cpf,

        @NotBlank
        @Pattern(regexp = "\\d{5}")
        String matricula,

        @NotNull
        TipoCarta tipoCarta,

        @NotNull
        @Valid
        DadosEndereco endereco) {
}
