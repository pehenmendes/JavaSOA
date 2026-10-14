package br.com.fiap3espg.autoescola.adapter.in.controller.response.usuario;

import br.com.fiap3espg.autoescola.application.core.domain.Usuario;
import br.com.fiap3espg.autoescola.common.vo.enumeration.Role;

public record DadosDetalhamentoUsuario(
        Long id,
        String login,
        String senha,
        Role perfil,
        boolean ativo
) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getSenha(),
                usuario.getPerfil(),
                usuario.isAtivo()
        );
    }
}
