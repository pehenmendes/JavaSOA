package br.com.fiap3espg.autoescola.infra.security;

import br.com.fiap3espg.autoescola.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final TokenService tokenService;
    private final UsuarioRepository repository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String tokenJWT = recuperarToken(request);
        if (tokenJWT != null) {
            try {
                String subject = tokenService.getSubject(tokenJWT);
                UserDetails usuario = repository.findByLogin(subject);

                if (usuario == null || !usuario.isEnabled()) {
                    responderNaoAutorizado(response);
                    return;
                }

                var authentication = new UsernamePasswordAuthenticationToken(
                        usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (RuntimeException ex) {
                SecurityContextHolder.clearContext();
                responderNaoAutorizado(response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        }
        return null;
    }

    private void responderNaoAutorizado(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"message\":\"Token inválido, expirado ou usuário inativo\"}");
    }
}
