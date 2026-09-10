package br.com.fiap3espg.autoescola.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroUsuario(
        String login,
        String senha,
        Role perfil
) { }
