package br.com.fiap3espg.autoescola.service;

import br.com.fiap3espg.autoescola.domain.usuario.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    public DadosDetalhamentoUsuario cadastrarUsuario(DadosCadastroUsuario dados) {
        Usuario usuario = new Usuario(dados);
        Usuario saved = repository.save(usuario);
        return new DadosDetalhamentoUsuario(saved);
    }

    public Page<DadosListagemUsuario> listarUsuarios(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemUsuario::new);
    }

    public DadosDetalhamentoUsuario detalharUsuario(Long id) {
        Usuario usuario = repository
                .findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("ID do usuário não existe"));
        return new DadosDetalhamentoUsuario(usuario);
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizarUsuario(DadosAtualizacaoUsuario dados) {
        Usuario usuario = repository
                .findById(dados.id())
                .orElseThrow(() -> new UsuarioNotFoundException("ID do usuário não existe"));
        usuario.atualizarInformacoes(dados);
        Usuario saved = repository.save(usuario);
        return new DadosDetalhamentoUsuario(saved);
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizarSenha(DadosAtualizarSenhaUsuario dados) {
        Usuario usuario = repository
                .findById(dados.id())
                .orElseThrow(() -> new UsuarioNotFoundException("ID do usuário não existe"));
        if (!encoder.matches(dados.senhaAtual(), usuario.getSenha())) {
            throw new SenhaIguaisException("Utilize uma senha diferente da anterior");
        }
        usuario.atualizarSenha(dados);
        Usuario saved = repository.save(usuario);
        return new DadosDetalhamentoUsuario(saved);
    }

    public void excluirUsuario(Long id) {
        Usuario usuario = repository
                .findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("ID do usuário não existe"));
        usuario.excluir();
        repository.save(usuario);
    }

}
