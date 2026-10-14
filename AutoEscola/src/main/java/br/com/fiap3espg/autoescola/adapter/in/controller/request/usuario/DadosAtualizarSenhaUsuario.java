package br.com.fiap3espg.autoescola.adapter.in.controller.request.usuario;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizarSenhaUsuario(
        @NotNull
        Long id,
        String senhaAtual,
        String senhaNova
) {
}
