package br.com.fiap3espg.autoescola.domain.instrucao.validacao;

import br.com.fiap3espg.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.domain.instrucao.ValidacaoException;
import br.com.fiap3espg.autoescola.domain.instrutor.InstrutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidadorInstrutorAtivo implements ValidadorAgendamento{
    private final InstrutorRepository instrutorRepository;

    @Override
    public void validar(DadosAgendamentoInstrucao dados) {
        if (instrutorRepository.existsByIdAndAtivoFalse(dados.idInstrutor())) {
            throw new ValidacaoException("Não é possivel agendar instrução com instrutor inativo");
        }
    }
}
