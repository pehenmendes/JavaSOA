package br.com.fiap3espg.autoescola.domain.instrucao.validacao;

import br.com.fiap3espg.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.domain.instrucao.InstrucaoRepository;
import br.com.fiap3espg.autoescola.domain.instrucao.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidadorConflitoHorarioInstrutor implements ValidadorAgendamento {
    private final InstrucaoRepository repository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        // Sem instrutor informado, a escolha automática já considera apenas instrutores livres.
        if (dados.idInstrutor() == null) {
            return;
        }

        boolean instrutorOcupado = repository
                .existsByInstrutorIdAndDataHoraAndAtivoTrue(dados.idInstrutor(), dados.dataHora());

        if (instrutorOcupado) {
            throw new ValidacaoException("Instrutor ocupado na data/hora escolhida");
        }
    }
}
