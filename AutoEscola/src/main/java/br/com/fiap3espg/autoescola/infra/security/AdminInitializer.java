package br.com.fiap3espg.autoescola.infra.security;

import br.com.fiap3espg.autoescola.domain.usuario.Role;
import br.com.fiap3espg.autoescola.domain.usuario.Usuario;
import br.com.fiap3espg.autoescola.domain.usuario.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
public class AdminInitializer {
    @Value("${admin.senha}")
    private String senha;

    @Bean
    CommandLineRunner criarAdmin(
            UsuarioRepository repository,
            PasswordEncoder encoder
    ) {
        return args -> {
            if (repository.findByLogin("admin") == null){
                String senhaCriptografada =
                        encoder.encode(senha);

                Usuario admin = new Usuario(
                        "admin",
                        senhaCriptografada,
                        Role.ADMIN
                );

                repository.save(admin);

                log.info("Usuário ADMIN criado com sucesso!");
            }
        };
    }
}
