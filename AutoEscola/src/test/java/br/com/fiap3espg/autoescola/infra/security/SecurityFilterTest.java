package br.com.fiap3espg.autoescola.infra.security;

import br.com.fiap3espg.autoescola.domain.usuario.Role;
import br.com.fiap3espg.autoescola.domain.usuario.Usuario;
import br.com.fiap3espg.autoescola.domain.usuario.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {
    @Mock
    TokenService tokenService;

    @Mock
    UsuarioRepository repository;

    @InjectMocks
    SecurityFilter filter;

    MockHttpServletRequest request = new MockHttpServletRequest();
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Sem cabeçalho Authorization: segue a cadeia sem autenticar")
    void semCabecalho() throws Exception {
        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(tokenService, repository);
    }

    @Test
    @DisplayName("Cabeçalho que não é Bearer é ignorado")
    void cabecalhoNaoBearer() throws Exception {
        request.addHeader("Authorization", "Basic abc123");

        filter.doFilter(request, response, chain);

        assertThat(chain.getRequest()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("Token válido autentica o usuário com suas authorities")
    void tokenValido() throws Exception {
        request.addHeader("Authorization", "Bearer token-bom");
        when(tokenService.getSubject("token-bom")).thenReturn("maria");
        when(repository.findByLogin("maria")).thenReturn(new Usuario("maria", "hash", Role.ADMIN));

        filter.doFilter(request, response, chain);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(chain.getRequest()).isNotNull();
        assertThat(auth).isNotNull();
        assertThat(auth.getName()).isEqualTo("maria");
        assertThat(auth.getAuthorities()).extracting(Object::toString).containsExactly("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Token inválido responde 401 e interrompe a cadeia")
    void tokenInvalido() throws Exception {
        request.addHeader("Authorization", "Bearer token-ruim");
        when(tokenService.getSubject("token-ruim")).thenThrow(new RuntimeException("Token JWT inválido/expirado!"));

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(chain.getRequest()).isNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("Usuário do token não existe mais: 401, sem NullPointerException")
    void usuarioInexistente() throws Exception {
        request.addHeader("Authorization", "Bearer token-bom");
        when(tokenService.getSubject("token-bom")).thenReturn("fantasma");
        when(repository.findByLogin("fantasma")).thenReturn(null);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(chain.getRequest()).isNull();
    }

    @Test
    @DisplayName("Usuário desativado com token ainda válido: 401")
    void usuarioInativo() throws Exception {
        Usuario inativo = new Usuario("maria", "hash", Role.USER);
        inativo.excluir();
        request.addHeader("Authorization", "Bearer token-bom");
        when(tokenService.getSubject("token-bom")).thenReturn("maria");
        when(repository.findByLogin("maria")).thenReturn(inativo);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(401);
        assertThat(chain.getRequest()).isNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
