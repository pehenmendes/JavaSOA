package br.com.fiap3espg.autoescola.adapter.in.controller.request.usuario;

import br.com.fiap3espg.autoescola.common.vo.enumeration.Role;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoUsuario(
        @NotNull
        Long id,
        String login,
        Role perfil
) {
}
