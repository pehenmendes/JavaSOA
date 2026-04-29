package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.instrutor.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instrutores")
public class InstrutorController {
    @Autowired
    private InstrutorRepository repository;

    @PostMapping
    @Transactional
    public void cadastrarInstrutor(@RequestBody @Valid DadosCadastroInstrutor dados) {
        Instrutor instrutor = new Instrutor(dados);
        repository.save(instrutor);
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemInstrutor>> listarInstrutores(@PageableDefault(size=5) Pageable paginacao) {
        Page page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemInstrutor::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoInstrutor> detalharInstrutor(@PathVariable Long id) {
        Instrutor instrutor = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoInstrutor(instrutor));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoInstrutor> atualizarInstrutor(@RequestBody @Valid DadosAtualizacaoInstrutor dados) {
        Instrutor instrutor = repository.getReferenceById(dados.id());
        instrutor.atualizarInformacoes(dados);
        Instrutor saved = repository.save(instrutor);
        return ResponseEntity.ok(new DadosDetalhamentoInstrutor(saved));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluirInstrutor(@PathVariable Long id){
        Instrutor instrutor = repository.getReferenceById(id);
        instrutor.excluir();
        repository.save(instrutor);
        return ResponseEntity.noContent().build();
    }
}
