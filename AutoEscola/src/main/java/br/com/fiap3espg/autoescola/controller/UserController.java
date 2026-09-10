package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.domain.usuario.*;
import br.com.fiap3espg.autoescola.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosDetalhamentoUsuario> cadastrarUsuario(
            @RequestBody @Valid DadosCadastroUsuario dados,
            UriComponentsBuilder uriBuilder
    ) {
        DadosDetalhamentoUsuario dto = service.cadastrarUsuario(dados);
        URI uri = uriBuilder
                .path("/usuarios/{id}")
                .buildAndExpand(dto.id())
                .toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<DadosListagemUsuario>> listarUsuarios(
            @PageableDefault(size=5) Pageable paginacao
    ) {
        return ResponseEntity.ok(service.listarUsuarios(paginacao));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosDetalhamentoUsuario> detalharUsuarios(@PathVariable Long id){
        return ResponseEntity.ok(service.detalharUsuario(id));
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosDetalhamentoUsuario> atualizarUsuario(
            @RequestBody @Valid DadosAtualizacaoUsuario dados
    ){
        return ResponseEntity.ok(service.atualizarUsuario(dados));
    }

    @PatchMapping("/{id}/senha")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<DadosDetalhamentoUsuario> atualizarSenhaUsuario(
            @RequestBody @Valid DadosAtualizarSenhaUsuario dados,
            Authentication authentication
    ) {
        return ResponseEntity.ok(service.atualizarSenha(dados, authentication));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluirUsuario(@PathVariable Long id){
        service.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
