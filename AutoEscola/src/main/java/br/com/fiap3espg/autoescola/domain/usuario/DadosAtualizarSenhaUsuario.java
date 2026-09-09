package br.com.fiap3espg.autoescola.domain.usuario;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizarSenhaUsuario(
        @NotNull
        Long id,
        String senha
) {
}
