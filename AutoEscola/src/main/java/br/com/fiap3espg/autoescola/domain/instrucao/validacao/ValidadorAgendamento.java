package br.com.fiap3espg.autoescola.domain.instrucao.validacao;

import br.com.fiap3espg.autoescola.domain.instrucao.DadosAgendamentoInstrucao;

public interface ValidadorAgendamento {
    void validar(DadosAgendamentoInstrucao dados);
}
