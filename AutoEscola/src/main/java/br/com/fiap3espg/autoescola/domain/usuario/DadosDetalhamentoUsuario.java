package br.com.fiap3espg.autoescola.domain.usuario;

public record DadosDetalhamentoUsuario(
        Long id,
        String login,
        String senha,
        Role perfil
) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getSenha(),
                usuario.getPerfil()
        );
    }
}
