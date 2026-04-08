package br.com.fiap3espg.autoescola.instrutor;

import br.com.fiap3espg.autoescola.endereco.EnderecoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record InstrutorDTO(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Pattern(regexp = "\\d{9,11}")
        String cnh,

        @NotNull
        Especialidade especialidade,

        @Valid
        EnderecoDTO endereco) {
}
