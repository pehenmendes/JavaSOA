package br.com.fiap3espg.autoescola.application.core.specification;

import br.com.fiap3espg.autoescola.application.core.specification.contract.ValidadorAgendamento;
import br.com.fiap3espg.autoescola.adapter.in.controller.request.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.exception.type.ValidacaoException;
import br.com.fiap3espg.autoescola.application.port.out.InstrutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidadorInstrutorAtivo implements ValidadorAgendamento {
    private final InstrutorRepository instrutorRepository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        if (instrutorRepository.existsByIdAndAtivoFalse(dados.idInstrutor())) {
            throw new ValidacaoException("Não é possivel agendar instrução com instrutor inativo");
        }
    }
}
