package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.domain.instrutor.*;
import br.com.fiap3espg.autoescola.service.InstrutorService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/instrutores")
public class InstrutorController {
    @Autowired
    private InstrutorService service;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoInstrutor> cadastrarInstrutor(
            @RequestBody @Valid DadosCadastroInstrutor dados,
            UriComponentsBuilder uriBuilder) {
        URI uri = uriBuilder.buildAndExpand("instrutores/{id}").toUri();
        return ResponseEntity.created(uri).body(service.cadastrarInstrutor(dados));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemInstrutor>> listarInstrutores(
            @PageableDefault(size=5) Pageable paginacao) {
        return ResponseEntity.ok(service.listarInstrutores(paginacao));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoInstrutor> detalharInstrutor(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalharInstrutor(id));
    }

    @PutMapping
    public ResponseEntity<DadosDetalhamentoInstrutor> atualizarInstrutor(
            @RequestBody @Valid DadosAtualizacaoInstrutor dados) {
        return ResponseEntity.ok(service.atualizarInstrutor(dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirInstrutor(@PathVariable Long id){
        service.deleteInstrutor(id);
        return ResponseEntity.noContent().build();
    }
}
