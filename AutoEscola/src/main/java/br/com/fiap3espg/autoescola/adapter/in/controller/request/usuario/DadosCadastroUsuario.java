package br.com.fiap3espg.autoescola.adapter.in.controller.request.usuario;

import br.com.fiap3espg.autoescola.common.vo.enumeration.Role;

public record DadosCadastroUsuario(
        String login,
        String senha,
        Role perfil
) { }
