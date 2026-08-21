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
        Aluno aluno = repository
                .findById(id)
                .orElseThrow(() -> new AlunoNotFoundException("ID do aluno não existe!"));
        return new DadosDetalhamentoAluno(aluno);
    }

    @Transactional
    public DadosDetalhamentoAluno atualizarAluno(DadosAtualizacaoAluno dados) {
        Aluno aluno = repository
                .findById(dados.id())
                .orElseThrow(() -> new AlunoNotFoundException("ID do aluno não existe!"));
        aluno.atualizarInformacoes(dados);
        Aluno saved = repository.save(aluno);
        return new DadosDetalhamentoAluno(saved);
    }

    public void excluirAluno(Long id) {
        Aluno aluno = repository
                .findById(id)
                .orElseThrow(() -> new AlunoNotFoundException("ID do aluno não existe!"));
        aluno.excluir();
        repository.save(aluno);
    }
}
