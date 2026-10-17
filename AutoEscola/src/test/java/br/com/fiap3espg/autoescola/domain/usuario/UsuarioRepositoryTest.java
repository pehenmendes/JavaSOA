package br.com.fiap3espg.autoescola.domain.usuario;

import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UsuarioRepositoryTest {
    private static final String HASH = "$2a$10$abcdefghijklmnopqrstuuABCDEFGHIJKLMNOPQRSTUVWXYZ0123";

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager em;

    @Test
    @DisplayName("findByLogin retorna o usuário com o perfil gravado")
    void findByLogin() {
        em.persistAndFlush(new Usuario("repo.admin", HASH, Role.ADMIN));
        em.clear();

        UserDetails encontrado = repository.findByLogin("repo.admin");

        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getUsername()).isEqualTo("repo.admin");
        assertThat(encontrado.getAuthorities()).extracting(Object::toString).containsExactly("ROLE_ADMIN");
        assertThat(encontrado.isEnabled()).isTrue();
    }

    @Test
    @DisplayName("findByLogin retorna null quando o login não existe")
    void findByLoginInexistente() {
        assertThat(repository.findByLogin("nao.existe.mesmo")).isNull();
    }

    @Test
    @DisplayName("findAllByAtivoTrue não retorna usuários excluídos logicamente")
    void listarSomenteAtivos() {
        Usuario ativo = em.persist(new Usuario("repo.ativo", HASH, Role.USER));
        Usuario inativo = new Usuario("repo.inativo", HASH, Role.USER);
        inativo.excluir();
        em.persist(inativo);
        em.flush();

        var pagina = repository.findAllByAtivoTrue(PageRequest.of(0, 100));

        assertThat(pagina.getContent()).contains(ativo).doesNotContain(inativo);
    }

    @Test
    @DisplayName("Usuário excluído logicamente fica desabilitado ao ser recarregado")
    void inativoFicaDesabilitado() {
        Usuario usuario = new Usuario("repo.desabilitado", HASH, Role.USER);
        usuario.excluir();
        em.persistAndFlush(usuario);
        em.clear();

        assertThat(repository.findByLogin("repo.desabilitado").isEnabled()).isFalse();
    }

    @Test
    @DisplayName("Login duplicado viola a constraint unique")
    void loginDuplicado() {
        em.persistAndFlush(new Usuario("repo.duplicado", HASH, Role.USER));

        assertThatThrownBy(() -> em.persistAndFlush(new Usuario("repo.duplicado", HASH + "x", Role.USER)))
                .isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Usuários diferentes podem ter o mesmo hash (unique removido na V9)")
    void mesmoHashPermitido() {
        em.persist(new Usuario("repo.hash1", HASH, Role.USER));
        em.persistAndFlush(new Usuario("repo.hash2", HASH, Role.USER));

        assertThat(repository.findByLogin("repo.hash1")).isNotNull();
        assertThat(repository.findByLogin("repo.hash2")).isNotNull();
    }
}
