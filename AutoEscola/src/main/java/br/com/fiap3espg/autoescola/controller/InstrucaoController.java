package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.domain.aluno.Aluno;
import br.com.fiap3espg.autoescola.domain.aluno.AlunoRepository;
import br.com.fiap3espg.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.domain.instrucao.DadosDetalhamentoAgendamento;
import br.com.fiap3espg.autoescola.domain.instrucao.Instrucao;
import br.com.fiap3espg.autoescola.domain.instrucao.InstrucaoRepository;
import br.com.fiap3espg.autoescola.domain.instrutor.Instrutor;
import br.com.fiap3espg.autoescola.domain.instrutor.InstrutorRepository;
import br.com.fiap3espg.autoescola.service.InstrucaoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/instrucoes")
@RequiredArgsConstructor
public class InstrucaoController {
    private final InstrucaoService service;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoAgendamento> agendarInstrucao(@RequestBody @Valid DadosAgendamentoInstrucao dados) {
        return ResponseEntity.ok(service.agendarInstrucao(dados));
    }
}
