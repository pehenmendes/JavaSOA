package br.com.fiap3espg.autoescola.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(
        @NotBlank
        @Size(max = 100)
        String login,

        @NotBlank
        @Size(min = 6, max = 72)
        String senha,

        Role perfil
) { }
