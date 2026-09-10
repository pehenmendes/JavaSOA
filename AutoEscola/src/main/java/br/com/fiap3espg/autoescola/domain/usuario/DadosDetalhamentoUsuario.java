package br.com.fiap3espg.autoescola.domain.usuario;

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
