package br.com.fiap3espg.autoescola.service;

import br.com.fiap3espg.autoescola.domain.instrutor.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class InstrutorService {
    @Autowired
    private InstrutorRepository repository;

    @Transactional
    public DadosDetalhamentoInstrutor cadastrarInstrutor(DadosCadastroInstrutor dados) {
        Instrutor instrutor = new Instrutor(dados);
        Instrutor saved = repository.save(instrutor);
        return new DadosDetalhamentoInstrutor(saved);
    }

    public Page<DadosListagemInstrutor> listarInstrutores(Pageable paginacao) {
         return repository.findAllByAtivoTrue(paginacao).map(DadosListagemInstrutor::new);
    }

    public DadosDetalhamentoInstrutor detalharInstrutor(Long id) {
        Instrutor instrutor = repository.getReferenceById(id);
        return new DadosDetalhamentoInstrutor(instrutor);
    }

    @Transactional
    public DadosDetalhamentoInstrutor atualizarInstrutor(@Valid DadosAtualizacaoInstrutor dados) {
        Instrutor instrutor = repository.getReferenceById(dados.id());
        instrutor.atualizarInformacoes(dados);
        Instrutor saved = repository.save(instrutor);
        return new DadosDetalhamentoInstrutor(saved);
    }

    @Transactional
    public void deleteInstrutor(Long id) {
        Instrutor instrutor = repository.getReferenceById(id);
        instrutor.excluir();
        repository.save(instrutor);
    }
}
