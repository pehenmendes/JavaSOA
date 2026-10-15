package br.com.fiap3espg.autoescola.domain.instrucao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface InstrucaoRepository extends JpaRepository<Instrucao, Long> {
    boolean existsByInstrutorIdAndDataHoraAndAtivoTrue(Long idInstrutor, LocalDateTime dataHora);

    boolean existsByAlunoIdAndDataHoraBetweenAndAtivoTrue(Long idAluno, LocalDateTime inicio, LocalDateTime fim);
}
