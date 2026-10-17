package br.com.fiap3espg.autoescola.domain.instrucao;

import br.com.fiap3espg.autoescola.domain.aluno.Aluno;
import br.com.fiap3espg.autoescola.domain.instrutor.Especialidade;
import br.com.fiap3espg.autoescola.domain.instrutor.Instrutor;
import br.com.fiap3espg.autoescola.util.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InstrucaoTest {
    private final LocalDateTime hora = LocalDateTime.of(2030, 5, 6, 10, 0);

    private Instrucao instrucao(Long id) {
        Aluno aluno = Fixtures.alunoComId(1L, 1);
        Instrutor instrutor = Fixtures.instrutorComId(1L, 1, Especialidade.MOTOS);
        return new Instrucao(id, aluno, instrutor, hora, true);
    }

    @Test
    @DisplayName("Nova instrução nasce ativa")
    void nasceAtiva() {
        assertThat(instrucao(null).isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Cancelar marca a instrução como inativa")
    void cancelar() {
        Instrucao i = instrucao(1L);

        i.cancelar();

        assertThat(i.isAtivo()).isFalse();
    }

    @Test
    @DisplayName("Cancelar duas vezes é idempotente")
    void cancelarDuasVezes() {
        Instrucao i = instrucao(1L);

        i.cancelar();
        i.cancelar();

        assertThat(i.isAtivo()).isFalse();
    }

    @Test
    @DisplayName("Igualdade considera apenas o id")
    void igualdadePorId() {
        assertThat(instrucao(1L)).isEqualTo(instrucao(1L)).isNotEqualTo(instrucao(2L));
    }

    @Test
    @DisplayName("DTO de detalhamento traz nomes, especialidade e data")
    void detalhamento() {
        var dto = new DadosDetalhamentoAgendamento(instrucao(9L));

        assertThat(dto.id()).isEqualTo(9L);
        assertThat(dto.nomeAluno()).isEqualTo("Aluno 1");
        assertThat(dto.nomeInstrutor()).isEqualTo("Instrutor 1");
        assertThat(dto.especialidade()).isEqualTo(Especialidade.MOTOS);
        assertThat(dto.dataHora()).isEqualTo(hora);
        assertThat(dto.ativo()).isTrue();
    }
}
