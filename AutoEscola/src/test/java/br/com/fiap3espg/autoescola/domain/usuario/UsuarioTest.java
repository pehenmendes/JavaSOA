package br.com.fiap3espg.autoescola.domain.usuario;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioTest {

    @Test
    @DisplayName("Cadastro sem perfil assume USER")
    void perfilPadrao() {
        Usuario u = new Usuario(new DadosCadastroUsuario("ana", "senha123", null), "hash");

        assertThat(u.getPerfil()).isEqualTo(Role.USER);
        assertThat(u.getLogin()).isEqualTo("ana");
        assertThat(u.getSenha()).isEqualTo("hash");
        assertThat(u.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Cadastro com perfil ADMIN é respeitado")
    void perfilAdmin() {
        Usuario u = new Usuario(new DadosCadastroUsuario("root", "senha123", Role.ADMIN), "hash");

        assertThat(u.getPerfil()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("Authorities usam o prefixo ROLE_")
    void authorities() {
        Usuario admin = new Usuario("a", "h", Role.ADMIN);
        Usuario user = new Usuario("u", "h", Role.USER);

        assertThat(admin.getAuthorities()).extracting(Object::toString).containsExactly("ROLE_ADMIN");
        assertThat(user.getAuthorities()).extracting(Object::toString).containsExactly("ROLE_USER");
    }

    @Test
    @DisplayName("UserDetails expõe login e senha criptografada")
    void userDetails() {
        Usuario u = new Usuario("ana", "hash", Role.USER);

        assertThat(u.getUsername()).isEqualTo("ana");
        assertThat(u.getPassword()).isEqualTo("hash");
        assertThat(u.isAccountNonExpired()).isTrue();
        assertThat(u.isAccountNonLocked()).isTrue();
        assertThat(u.isCredentialsNonExpired()).isTrue();
    }

    @Test
    @DisplayName("Usuário ativo está habilitado; após excluir, fica desabilitado")
    void habilitado() {
        Usuario u = new Usuario("ana", "hash", Role.USER);
        assertThat(u.isEnabled()).isTrue();

        u.excluir();

        assertThat(u.isAtivo()).isFalse();
        assertThat(u.isEnabled()).isFalse();
    }

    @Test
    @DisplayName("Atualização altera login e perfil")
    void atualizacao() {
        Usuario u = new Usuario("ana", "hash", Role.USER);

        u.atualizarInformacoes(new DadosAtualizacaoUsuario(1L, "ana.maria", Role.ADMIN));

        assertThat(u.getLogin()).isEqualTo("ana.maria");
        assertThat(u.getPerfil()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("Atualização com login em branco ou perfil nulo mantém os valores")
    void atualizacaoIgnoraVazios() {
        Usuario u = new Usuario("ana", "hash", Role.ADMIN);

        u.atualizarInformacoes(new DadosAtualizacaoUsuario(1L, "  ", null));

        assertThat(u.getLogin()).isEqualTo("ana");
        assertThat(u.getPerfil()).isEqualTo(Role.ADMIN);
    }

    @Test
    @DisplayName("atualizarSenha troca o hash, e ignora valor vazio")
    void atualizarSenha() {
        Usuario u = new Usuario("ana", "hashAntigo", Role.USER);

        u.atualizarSenha("hashNovo");
        assertThat(u.getSenha()).isEqualTo("hashNovo");

        u.atualizarSenha(" ");
        u.atualizarSenha(null);
        assertThat(u.getSenha()).isEqualTo("hashNovo");
    }

    @Test
    @DisplayName("DTOs de usuário não expõem a senha")
    void dtosSemSenha() {
        Usuario u = new Usuario("ana", "hash", Role.USER);

        assertThat(new DadosDetalhamentoUsuario(u).getClass().getRecordComponents())
                .noneMatch(c -> c.getName().equals("senha"));
        assertThat(new DadosListagemUsuario(u).getClass().getRecordComponents())
                .noneMatch(c -> c.getName().equals("senha"));
    }
}
