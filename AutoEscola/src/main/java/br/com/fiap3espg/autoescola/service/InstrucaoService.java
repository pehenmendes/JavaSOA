package br.com.fiap3espg.autoescola.service;

import br.com.fiap3espg.autoescola.domain.aluno.Aluno;
import br.com.fiap3espg.autoescola.domain.aluno.AlunoNotFoundException;
import br.com.fiap3espg.autoescola.domain.aluno.AlunoRepository;
import br.com.fiap3espg.autoescola.domain.instrucao.*;
import br.com.fiap3espg.autoescola.domain.instrutor.Instrutor;
import br.com.fiap3espg.autoescola.domain.instrutor.InstrutorNotFoundException;
import br.com.fiap3espg.autoescola.domain.instrutor.InstrutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstrucaoService {
    private final InstrucaoRepository repository;
    private final AlunoRepository alunoRepository;
    private final InstrutorRepository instrutorRepository;

    public DadosDetalhamentoAgendamento agendarInstrucao(DadosAgendamentoInstrucao dados) {
        if (!alunoRepository.existsById(dados.idAluno())) {
            throw new AlunoNotFoundException("ID do aluno informado não existe");
        }
        if (dados.idInstrutor() != null && !instrutorRepository.existsById(dados.idInstrutor())){
            throw new InstrutorNotFoundException("ID do instrutor informado não existe");
        }
        Aluno aluno = alunoRepository.getReferenceById(dados.idAluno());
        Instrutor instrutor = escolherInstrutor(dados);

        if (instrutor == null) {
            throw new ValidacaoException("Nenhum instrutor disponivel para a data/hora informada");
        }

        Instrucao instrucao = new Instrucao(null, aluno, instrutor, dados.dataHora());
        Instrucao salvo = repository.save(instrucao);
        return new DadosDetalhamentoAgendamento(salvo);
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
