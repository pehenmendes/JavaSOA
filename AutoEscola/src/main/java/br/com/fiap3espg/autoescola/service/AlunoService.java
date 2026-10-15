package br.com.fiap3espg.autoescola.service;

import br.com.fiap3espg.autoescola.domain.aluno.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private static final String ALUNO_NAO_EXISTE = "ID do aluno não existe!";

    private final AlunoRepository repository;

    @Transactional
    public DadosDetalhamentoAluno cadastrarAluno(DadosCadastroAluno dados) {
        Aluno aluno = new Aluno(dados);
        Aluno saved = repository.save(aluno);
        return new DadosDetalhamentoAluno(saved);
    }

    public Page<DadosListagemAluno> listarAlunos(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemAluno::new);
    }

    public DadosDetalhamentoAluno detalharAluno(Long id) {
        return new DadosDetalhamentoAluno(buscarAtivo(id));
    }

    @Transactional
    public DadosDetalhamentoAluno atualizarAluno(DadosAtualizacaoAluno dados) {
        Aluno aluno = buscarAtivo(dados.id());
        aluno.atualizarInformacoes(dados);
        Aluno saved = repository.save(aluno);
        return new DadosDetalhamentoAluno(saved);
    }

    @Transactional
    public void excluirAluno(Long id) {
        Aluno aluno = buscarAtivo(id);
        aluno.excluir();
        repository.save(aluno);
    }

    private Aluno buscarAtivo(Long id) {
        return repository
                .findById(id)
                .filter(Aluno::isAtivo)
                .orElseThrow(() -> new AlunoNotFoundException(ALUNO_NAO_EXISTE));
    }
}
