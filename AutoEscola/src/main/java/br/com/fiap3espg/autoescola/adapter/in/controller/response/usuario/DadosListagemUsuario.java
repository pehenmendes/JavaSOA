package br.com.fiap3espg.autoescola.adapter.in.controller.response.usuario;

import br.com.fiap3espg.autoescola.application.core.domain.Usuario;
import br.com.fiap3espg.autoescola.common.vo.enumeration.Role;

public record DadosListagemUsuario(
        Long id,
        String login,
        Role perfil,
        boolean ativo
) {
    public DadosListagemUsuario(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getPerfil(),
                usuario.isAtivo()
        );
    }
}
