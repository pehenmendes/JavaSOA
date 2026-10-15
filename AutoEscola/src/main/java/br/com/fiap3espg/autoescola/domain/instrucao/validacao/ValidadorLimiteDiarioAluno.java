package br.com.fiap3espg.autoescola.domain.instrucao.validacao;

import br.com.fiap3espg.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.domain.instrucao.InstrucaoRepository;
import br.com.fiap3espg.autoescola.domain.instrucao.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class ValidadorLimiteDiarioAluno implements ValidadorAgendamento {
    private final InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        LocalDate dia = dados.dataHora().toLocalDate();
        LocalDateTime inicioDoDia = dia.atStartOfDay();
        LocalDateTime fimDoDia = dia.atTime(LocalTime.MAX);

        boolean reincidencia = repository
                .existsByAlunoIdAndDataHoraBetweenAndAtivoTrue(dados.idAluno(), inicioDoDia, fimDoDia);

        if (reincidencia) {
            throw new ValidacaoException("Permitido o agendamento diário de apenas uma instrução por aluno");
        }
    }
}
