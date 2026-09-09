package br.com.fiap3espg.autoescola.domain.usuario;

public record DadosListagemUsuario(
        Long id,
        String login,
        Role perfil
) {
    public DadosListagemUsuario(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getLogin(),
                usuario.getPerfil()
        );
    }
}
