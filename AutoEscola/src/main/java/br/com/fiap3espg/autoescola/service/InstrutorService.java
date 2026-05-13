package br.com.fiap3espg.autoescola.service;

import br.com.fiap3espg.autoescola.domain.instrutor.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstrutorService {
    private final InstrutorRepository repository;

    @Transactional
    public DadosDetalhamentoInstrutor cadastrarInstrutor(DadosCadastroInstrutor dados) {
//        Instrutor instrutor = new Instrutor(dados);
//        Instrutor saved = repository.save(instrutor);
//        return new DadosDetalhamentoInstrutor(saved);
        return new DadosDetalhamentoInstrutor(repository.save(new Instrutor(dados)));
    }

    public Page<DadosListagemInstrutor> listarInstrutores(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemInstrutor::new);
    }

    public DadosDetalhamentoInstrutor detalharInstrutor(Long id) {
        Instrutor instrutor = repository.findById(id)
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do instrutor informado não exite!"));
        return new DadosDetalhamentoInstrutor(instrutor);
    }

    @Transactional
    public DadosDetalhamentoInstrutor atualizarInstrutor(@Valid DadosAtualizacaoInstrutor dados) {
        Instrutor instrutor = repository.findById(dados.id())
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do instrutor informado não exite!"));
        instrutor.atualizarInformacoes(dados);
        Instrutor saved = repository.save(instrutor);
        return new DadosDetalhamentoInstrutor(saved);
    }

    @Transactional
    public void deleteInstrutor(Long id) {
        Instrutor instrutor = repository.findById(id)
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do instrutor informado não exite!"));
        instrutor.excluir();
        repository.save(instrutor);
    }
}
