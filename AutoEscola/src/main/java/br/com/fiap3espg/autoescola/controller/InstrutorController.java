package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.instrutor.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public Page<DadosListagemInstrutor> listarInstrutores(@PageableDefault(size=5) Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemInstrutor::new);
    }

    @PutMapping
    @Transactional
    public void atualizarInstrutor(@RequestBody @Valid DadosAtualizacaoInstrutor dados) {
        Instrutor instrutor = repository.getReferenceById(dados.id());
        instrutor.atualizarInformacoes(dados);
        repository.save(instrutor);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluirInstrutor(@PathVariable Long id){
        repository.deleteById(id);
    }
}
