package br.com.fiap3espg.autoescola.application.service;

import br.com.fiap3espg.autoescola.adapter.in.controller.request.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.adapter.in.controller.response.instrucao.DadosDetalhamentoAgendamento;
import br.com.fiap3espg.autoescola.application.core.domain.Aluno;
import br.com.fiap3espg.autoescola.application.core.domain.Instrucao;
import br.com.fiap3espg.autoescola.application.port.out.InstrucaoRepository;
import br.com.fiap3espg.autoescola.exception.type.AlunoNotFoundException;
import br.com.fiap3espg.autoescola.exception.type.ValidacaoException;
import br.com.fiap3espg.autoescola.application.port.out.AlunoRepository;
import br.com.fiap3espg.autoescola.application.core.specification.contract.ValidadorAgendamento;
import br.com.fiap3espg.autoescola.application.core.domain.Instrutor;
import br.com.fiap3espg.autoescola.exception.type.InstrutorNotFoundException;
import br.com.fiap3espg.autoescola.application.port.out.InstrutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstrucaoService {
    private final InstrucaoRepository repository;
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;
    private final List<ValidadorAgendamento> validadoresAgendamento;

    public DadosDetalhamentoAgendamento agendarInstrucao(DadosAgendamentoInstrucao dados) {
        if (!alunoRepository.existsById(dados.idAluno())) {
            throw new AlunoNotFoundException("ID do aluno informado não existe");
        }
        if (dados.idInstrutor() != null && !instrutorRepository.existsById(dados.idInstrutor())){
            throw new InstrutorNotFoundException("ID do instrutor informado não existe");
        }
        // Validações
        validadoresAgendamento.forEach(validador -> validador.validar(dados));

        Aluno aluno = alunoRepository.getReferenceById(dados.idAluno());
        Instrutor instrutor = escolherInstrutor(dados);

        if (instrutor == null) {
            throw new ValidacaoException("Nenhum instrutor disponivel para a data/hora informada");
        }

        Instrucao instrucao = new Instrucao(null, aluno, instrutor, dados.dataHora(), true);
        Instrucao salvo = repository.save(instrucao);
        return new DadosDetalhamentoAgendamento(salvo);
    }

    public void cancelarInstrucao(Long id){
        Instrucao instrucao = repository
                .findById(id)
                .orElseThrow(() -> new InstrutorNotFoundException("ID do agendamento não existe"));
        instrucao.cancelar();
        repository.save(instrucao);
    }

    private Instrutor escolherInstrutor(DadosAgendamentoInstrucao dados) {
        if (dados.idInstrutor() != null) {
            return instrutorRepository.getReferenceById(dados.idInstrutor());
        }
        if (dados.especialidade() == null) {
            throw new ValidacaoException("Especialidade é obrigatória se o instrutor não for informado");
        }
        return instrutorRepository.escolherInstrutorAleatorioDisponivel(dados.especialidade(), dados.dataHora());
    }
}
