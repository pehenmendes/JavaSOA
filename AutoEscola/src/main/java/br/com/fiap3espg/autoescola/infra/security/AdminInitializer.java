package br.com.fiap3espg.autoescola.infra.security;

import br.com.fiap3espg.autoescola.domain.usuario.Role;
import br.com.fiap3espg.autoescola.domain.usuario.Usuario;
import br.com.fiap3espg.autoescola.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

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

                System.out.println("Usuário ADMIN criado com sucesso!");
            }
        };
    }
}
