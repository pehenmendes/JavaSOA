package br.com.fiap3espg.autoescola.service;

import br.com.fiap3espg.autoescola.domain.usuario.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String USUARIO_NAO_EXISTE = "ID do usuário não existe";

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    public DadosDetalhamentoUsuario cadastrarUsuario(DadosCadastroUsuario dados) {
        Usuario usuario = new Usuario(dados, encoder.encode(dados.senha()));
        Usuario saved = repository.save(usuario);
        return new DadosDetalhamentoUsuario(saved);
    }

    public Page<DadosListagemUsuario> listarUsuarios(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemUsuario::new);
    }

    public DadosDetalhamentoUsuario detalharUsuario(Long id) {
        return new DadosDetalhamentoUsuario(buscarAtivo(id));
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizarUsuario(DadosAtualizacaoUsuario dados) {
        Usuario usuario = buscarAtivo(dados.id());
        usuario.atualizarInformacoes(dados);
        Usuario saved = repository.save(usuario);
        return new DadosDetalhamentoUsuario(saved);
    }

    @Transactional
    public DadosDetalhamentoUsuario atualizarSenha(
            Long id, DadosAtualizarSenhaUsuario dados, Authentication auth) {
        Usuario usuarioAlvo = buscarAtivo(id);
        Usuario usuarioLogado = (Usuario) auth.getPrincipal();

        if (usuarioLogado.getPerfil() == Role.USER
                && !usuarioLogado.getId().equals(usuarioAlvo.getId())) {
            throw new AccessDeniedException("Você só pode alterar sua própria senha");
        }

        if (!encoder.matches(dados.senhaAtual(), usuarioAlvo.getSenha())) {
            throw new SenhaAtualIncorretaException("Senha atual incorreta");
        }

        if (dados.senhaAtual().equals(dados.senhaNova())) {
            throw new SenhaIguaisException("Utilize uma senha diferente da anterior");
        }

        usuarioAlvo.atualizarSenha(encoder.encode(dados.senhaNova()));
        Usuario saved = repository.save(usuarioAlvo);
        return new DadosDetalhamentoUsuario(saved);
    }

    @Transactional
    public void excluirUsuario(Long id) {
        Usuario usuario = buscarAtivo(id);
        usuario.excluir();
        repository.save(usuario);
    }

    private Usuario buscarAtivo(Long id) {
        return repository
                .findById(id)
                .filter(Usuario::isAtivo)
                .orElseThrow(() -> new UsuarioNotFoundException(USUARIO_NAO_EXISTE));
    }
}
