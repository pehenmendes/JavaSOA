package br.com.fiap3espg.autoescola.domain.instrucao.validacao;

import br.com.fiap3espg.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.domain.instrucao.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioFuncionamento implements ValidadorAgendamento{
    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        LocalDateTime dataEscolhida = dados.dataHora();
        boolean ehDomingo = dataEscolhida.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        boolean preAbertura = dataEscolhida.getHour() < 6;
        boolean posFechamento = dataEscolhida.getHour() > 21 - 1;

        if (ehDomingo || preAbertura || posFechamento) {
            throw new ValidacaoException("Tentativa de agendamento fora do horário de funcinamento");
        }
    }
}
