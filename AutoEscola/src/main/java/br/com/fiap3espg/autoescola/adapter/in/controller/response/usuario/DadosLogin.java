package br.com.fiap3espg.autoescola.adapter.in.controller.response.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosLogin(
        @NotBlank
        String login,
        @NotBlank
        String senha) {
}
