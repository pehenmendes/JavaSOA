package br.com.fiap3espg.autoescola.application.core.specification;

import br.com.fiap3espg.autoescola.application.core.specification.contract.ValidadorAgendamento;
import br.com.fiap3espg.autoescola.adapter.in.controller.request.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.exception.type.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidadorHoraInteira implements ValidadorAgendamento {
    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        LocalDateTime dataEscolhida = dados.dataHora();

        if (dataEscolhida.getMinute() != 0) {
            throw new ValidacaoException("O horário deve ser preenchido em horas inteiras (ex: 9:00)");
        }
    }
}
