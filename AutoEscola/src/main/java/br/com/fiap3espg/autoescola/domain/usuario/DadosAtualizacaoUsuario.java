package br.com.fiap3espg.autoescola.domain.usuario;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DadosAtualizacaoUsuario(
        @NotNull
        Long id,

        @Size(max = 100)
        String login,

        Role perfil
) {
}
