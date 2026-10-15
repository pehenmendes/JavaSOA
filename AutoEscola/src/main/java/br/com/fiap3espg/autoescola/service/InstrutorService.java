package br.com.fiap3espg.autoescola.service;

import br.com.fiap3espg.autoescola.domain.instrutor.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstrutorService {
    private static final String INSTRUTOR_NAO_EXISTE = "ID do instrutor informado não existe!";

    private final InstrutorRepository repository;

    @Transactional
    public DadosDetalhamentoInstrutor cadastrarInstrutor(DadosCadastroInstrutor dados) {
        return new DadosDetalhamentoInstrutor(repository.save(new Instrutor(dados)));
    }

    public Page<DadosListagemInstrutor> listarInstrutores(Pageable paginacao) {
        return repository.findAllByAtivoTrue(paginacao).map(DadosListagemInstrutor::new);
    }

    public DadosDetalhamentoInstrutor detalharInstrutor(Long id) {
        return new DadosDetalhamentoInstrutor(buscarAtivo(id));
    }

    @Transactional
    public DadosDetalhamentoInstrutor atualizarInstrutor(DadosAtualizacaoInstrutor dados) {
        Instrutor instrutor = buscarAtivo(dados.id());
        instrutor.atualizarInformacoes(dados);
        Instrutor saved = repository.save(instrutor);
        return new DadosDetalhamentoInstrutor(saved);
    }

    @Transactional
    public void deleteInstrutor(Long id) {
        Instrutor instrutor = buscarAtivo(id);
        instrutor.excluir();
        repository.save(instrutor);
    }

    private Instrutor buscarAtivo(Long id) {
        return repository.findById(id)
                .filter(Instrutor::isAtivo)
                .orElseThrow(() -> new InstrutorNotFoundException(INSTRUTOR_NAO_EXISTE));
    }
}
