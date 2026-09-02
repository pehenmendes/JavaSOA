package br.com.fiap3espg.autoescola.domain.instrucao.validacao;

import br.com.fiap3espg.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.domain.instrucao.InstrucaoRepository;
import br.com.fiap3espg.autoescola.domain.instrucao.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ValidadorLimiteDiarioAluno implements ValidadorAgendamento{
    private final InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        LocalDateTime inicioExpediente = dados.dataHora().withHour(6);
        LocalDateTime fimExpediente = dados.dataHora().withHour(21 - 1);

        boolean reincidencia = repository.existsByAlunoIdAndDataHoraBetween(dados.idAluno(),inicioExpediente,fimExpediente);

        if (reincidencia) {
            throw new ValidacaoException("Permitido o agendamento diário de apenas uma instrução por aluno");
        }
    }
}
