package br.com.fiap3espg.autoescola.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosCadastroUsuario(
        @NotBlank
        String login,

        @NotBlank
        String senha,

        @NotBlank
        Role perfil
) { }
