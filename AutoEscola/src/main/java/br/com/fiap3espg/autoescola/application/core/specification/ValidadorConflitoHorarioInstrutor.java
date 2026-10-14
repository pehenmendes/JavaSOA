package br.com.fiap3espg.autoescola.application.core.specification;

import br.com.fiap3espg.autoescola.application.core.specification.contract.ValidadorAgendamento;
import br.com.fiap3espg.autoescola.adapter.in.controller.request.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.application.port.out.InstrucaoRepository;
import br.com.fiap3espg.autoescola.exception.type.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidadorConflitoHorarioInstrutor implements ValidadorAgendamento {
    private final InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        boolean instrutorOcupado = repository.existsByInstrutorIdAndDataHora(dados.idInstrutor(), dados.dataHora());

        if (instrutorOcupado) {
            throw new ValidacaoException("Instrutor ocupado na data/hora escolhida");
        }
    }
}
