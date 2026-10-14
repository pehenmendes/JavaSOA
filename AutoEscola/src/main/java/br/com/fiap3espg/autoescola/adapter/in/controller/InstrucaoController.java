package br.com.fiap3espg.autoescola.adapter.in.controller;

import br.com.fiap3espg.autoescola.adapter.in.controller.request.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.adapter.in.controller.response.instrucao.DadosDetalhamentoAgendamento;
import br.com.fiap3espg.autoescola.application.service.InstrucaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instrucoes")
@RequiredArgsConstructor
public class InstrucaoController {
    private final InstrucaoService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DadosDetalhamentoAgendamento> agendarInstrucao(@RequestBody @Valid DadosAgendamentoInstrucao dados) {
        return ResponseEntity.ok(service.agendarInstrucao(dados));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancelarInstrucao(@PathVariable Long id){
        service.cancelarInstrucao(id);
        return ResponseEntity.noContent().build();
    }
}
