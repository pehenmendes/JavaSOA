package br.com.fiap3espg.autoescola.service;

import br.com.fiap3espg.autoescola.domain.usuario.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UsuarioRepository repository;

    PasswordEncoder encoder = new BCryptPasswordEncoder();
    UserService service;

    Usuario dono;
    Usuario outro;
    Usuario admin;

    @BeforeEach
    void setUp() {
        service = new UserService(repository, encoder);
        dono = usuario(1L, "dono", "senhaAntiga", Role.USER);
        outro = usuario(2L, "outro", "qualquer", Role.USER);
        admin = usuario(3L, "admin", "admin123", Role.ADMIN);
        lenient().when(repository.findById(1L)).thenReturn(Optional.of(dono));
        lenient().when(repository.save(org.mockito.ArgumentMatchers.any(Usuario.class)))
                .thenAnswer(i -> i.getArgument(0));
    }

    @Test
    @DisplayName("Troca a senha quando a senha atual está correta")
    void trocaSenha() {
        var dados = new DadosAtualizarSenhaUsuario("senhaAntiga", "senhaNova1");

        service.atualizarSenha(1L, dados, auth(dono));

        assertThat(encoder.matches("senhaNova1", dono.getSenha())).isTrue();
    }

    @Test
    @DisplayName("Rejeita quando a senha atual está incorreta")
    void senhaAtualIncorreta() {
        var dados = new DadosAtualizarSenhaUsuario("errada", "senhaNova1");

        assertThatThrownBy(() -> service.atualizarSenha(1L, dados, auth(dono)))
                .isInstanceOf(SenhaAtualIncorretaException.class);
        assertThat(encoder.matches("senhaAntiga", dono.getSenha())).isTrue();
    }

    @Test
    @DisplayName("Rejeita quando a nova senha é igual à anterior")
    void senhaIgual() {
        var dados = new DadosAtualizarSenhaUsuario("senhaAntiga", "senhaAntiga");

        assertThatThrownBy(() -> service.atualizarSenha(1L, dados, auth(dono)))
                .isInstanceOf(SenhaIguaisException.class);
    }

    @Test
    @DisplayName("USER não pode alterar a senha de outro usuário")
    void userNaoAlteraOutro() {
        var dados = new DadosAtualizarSenhaUsuario("senhaAntiga", "senhaNova1");

        assertThatThrownBy(() -> service.atualizarSenha(1L, dados, auth(outro)))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("ADMIN pode alterar a senha de qualquer usuário")
    void adminAltera() {
        var dados = new DadosAtualizarSenhaUsuario("senhaAntiga", "senhaNova1");

        service.atualizarSenha(1L, dados, auth(admin));

        assertThat(encoder.matches("senhaNova1", dono.getSenha())).isTrue();
    }

    @Test
    @DisplayName("Usuário inativo é tratado como inexistente")
    void usuarioInativo() {
        dono.excluir();

        assertThatThrownBy(() -> service.detalharUsuario(1L))
                .isInstanceOf(UsuarioNotFoundException.class);
    }

    @Test
    @DisplayName("O detalhamento não expõe o hash da senha")
    void detalhamentoSemSenha() {
        var dto = service.detalharUsuario(1L);

        assertThat(dto.getClass().getRecordComponents())
                .noneMatch(c -> c.getName().equals("senha"));
    }

    private Usuario usuario(Long id, String login, String senhaPura, Role perfil) {
        Usuario u = new Usuario(login, encoder.encode(senhaPura), perfil);
        ReflectionTestUtils.setField(u, "id", id);
        ReflectionTestUtils.setField(u, "ativo", true);
        return u;
    }

    private Authentication auth(Usuario u) {
        return new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities());
    }
}
