package br.com.fiap3espg.autoescola.domain.usuario;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static br.com.fiap3espg.autoescola.util.ValidacaoUtil.camposInvalidos;
import static org.assertj.core.api.Assertions.assertThat;

class DadosUsuarioValidacaoTest {

    @Test
    @DisplayName("Cadastro válido")
    void cadastroValido() {
        assertThat(camposInvalidos(new DadosCadastroUsuario("ana", "senha123", null))).isEmpty();
    }

    @Test
    @DisplayName("Cadastro exige login e senha")
    void cadastroObrigatorios() {
        assertThat(camposInvalidos(new DadosCadastroUsuario(null, null, null)))
                .containsExactlyInAnyOrder("login", "senha");
        assertThat(camposInvalidos(new DadosCadastroUsuario(" ", " ", Role.USER)))
                .containsExactlyInAnyOrder("login", "senha");
    }

    @Test
    @DisplayName("Senha muito curta ou muito longa é rejeitada")
    void tamanhoDaSenha() {
        assertThat(camposInvalidos(new DadosCadastroUsuario("ana", "12345", null))).contains("senha");
        assertThat(camposInvalidos(new DadosCadastroUsuario("ana", "x".repeat(73), null))).contains("senha");
        assertThat(camposInvalidos(new DadosCadastroUsuario("ana", "x".repeat(72), null))).isEmpty();
    }

    @Test
    @DisplayName("Troca de senha exige senha atual e nova senha válida")
    void trocaDeSenha() {
        assertThat(camposInvalidos(new DadosAtualizarSenhaUsuario("atual", "novaSenha1"))).isEmpty();
        assertThat(camposInvalidos(new DadosAtualizarSenhaUsuario(null, null)))
                .containsExactlyInAnyOrder("senhaAtual", "senhaNova");
        assertThat(camposInvalidos(new DadosAtualizarSenhaUsuario("atual", "123")))
                .containsExactly("senhaNova");
    }

    @Test
    @DisplayName("Atualização exige o id")
    void atualizacaoExigeId() {
        assertThat(camposInvalidos(new DadosAtualizacaoUsuario(null, "ana", null))).containsExactly("id");
        assertThat(camposInvalidos(new DadosAtualizacaoUsuario(1L, "ana", Role.USER))).isEmpty();
    }

    @Test
    @DisplayName("Login exige login e senha preenchidos")
    void login() {
        assertThat(camposInvalidos(new DadosLogin("ana", "x"))).isEmpty();
        assertThat(camposInvalidos(new DadosLogin("", ""))).containsExactlyInAnyOrder("login", "senha");
    }
}
