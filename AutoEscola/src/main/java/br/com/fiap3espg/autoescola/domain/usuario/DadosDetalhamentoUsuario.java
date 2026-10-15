package br.com.fiap3espg.autoescola.domain.usuario;

public record DadosDetalhamentoUsuario(
        Long id,
        String login,
        Role perfil,
        boolean ativo
) {
    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getPerfil(),
                usuario.isAtivo()
        );
    }
}
