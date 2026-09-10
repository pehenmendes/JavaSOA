package br.com.fiap3espg.autoescola.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "Usuario")
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String senha;

    @Enumerated(EnumType.STRING)
    private Role perfil = Role.USER;

    private boolean ativo = true;

    public Usuario(DadosCadastroUsuario dados, String senha){
        this.login = dados.login();
        this.senha = senha;
        if (dados.perfil() != null) {
            this.perfil = dados.perfil();
        }
    }

    public Usuario(String login, String senha, Role perfil) {
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
    }

    public void atualizarInformacoes(DadosAtualizacaoUsuario dados) {
        if (dados.login() != null && !dados.login().isBlank()) {
            this.login = dados.login();
        }
        if (dados.perfil() != null) {
            this.perfil = dados.perfil();
        }
    }

    public void atualizarSenha(String senhaAtual, String senhaNova) {
        if ( (senhaAtual != null && !senhaAtual.isBlank()) &&
                (senhaNova != null && !senhaNova.isBlank()) ){
            this.senha = senhaNova;
        }
    }

    public void excluir() {
        this.ativo = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + perfil.name()));
    }

    @Override
    public @Nullable String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (!ativo) {
            return false;
        }
        return true;
    }
}
