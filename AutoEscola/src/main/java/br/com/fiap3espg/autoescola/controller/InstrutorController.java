package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.domain.instrutor.*;
import br.com.fiap3espg.autoescola.service.InstrutorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/instrutores")
@RequiredArgsConstructor
public class InstrutorController {
    private final InstrutorService service;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoInstrutor> cadastrarInstrutor(
            @RequestBody @Valid DadosCadastroInstrutor dados,
            UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoInstrutor dto = service.cadastrarInstrutor(dados);
        URI uri = uriBuilder
                .path("/instrutores/{id}")
                .buildAndExpand(dto.id())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
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
