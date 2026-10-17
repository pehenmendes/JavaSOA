package br.com.fiap3espg.autoescola.infra.security;

import br.com.fiap3espg.autoescola.domain.usuario.Role;
import br.com.fiap3espg.autoescola.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenServiceTest {
    private static final String SECRET = "segredo-de-teste";

    TokenService service;
    Usuario usuario = new Usuario("maria", "hash", Role.USER);

    @BeforeEach
    void setUp() {
        service = new TokenService();
        ReflectionTestUtils.setField(service, "secret", SECRET);
    }

    @Test
    @DisplayName("Token gerado devolve o login como subject")
    void geraEValida() {
        String token = service.generateToken(usuario);

        assertThat(service.getSubject(token)).isEqualTo("maria");
    }

    @Test
    @DisplayName("Token tem emissor AutoEscola e expira em cerca de 30 minutos")
    void emissorEExpiracao() {
        String token = service.generateToken(usuario);
        var decodificado = JWT.decode(token);

        assertThat(decodificado.getIssuer()).isEqualTo("AutoEscola");
        Duration restante = Duration.between(Instant.now(), decodificado.getExpiresAtAsInstant());
        assertThat(restante).isBetween(Duration.ofMinutes(29), Duration.ofMinutes(31));
    }

    @Test
    @DisplayName("dataExpiration é 30 minutos à frente")
    void dataExpiration() {
        Duration diferenca = Duration.between(Instant.now(), service.dataExpiration());

        assertThat(diferenca).isBetween(Duration.ofMinutes(29), Duration.ofMinutes(31));
    }

    @Test
    @DisplayName("Token com texto aleatório é rejeitado")
    void tokenAleatorio() {
        assertThatThrownBy(() -> service.getSubject("isto-nao-e-um-jwt"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Token assinado com outro segredo é rejeitado")
    void outroSegredo() {
        String token = JWT.create()
                .withIssuer("AutoEscola")
                .withSubject("maria")
                .withExpiresAt(Instant.now().plusSeconds(600))
                .sign(Algorithm.HMAC256("outro-segredo"));

        assertThatThrownBy(() -> service.getSubject(token)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Token expirado é rejeitado")
    void expirado() {
        String token = JWT.create()
                .withIssuer("AutoEscola")
                .withSubject("maria")
                .withExpiresAt(Instant.now().minusSeconds(60))
                .sign(Algorithm.HMAC256(SECRET));

        assertThatThrownBy(() -> service.getSubject(token)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("Token de outro emissor é rejeitado")
    void outroEmissor() {
        String token = JWT.create()
                .withIssuer("OutroSistema")
                .withSubject("maria")
                .withExpiresAt(Instant.now().plusSeconds(600))
                .sign(Algorithm.HMAC256(SECRET));

        assertThatThrownBy(() -> service.getSubject(token)).isInstanceOf(RuntimeException.class);
    }
}
