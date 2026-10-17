package br.com.fiap3espg.autoescola.domain.instrucao;

import br.com.fiap3espg.autoescola.domain.instrutor.Especialidade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static br.com.fiap3espg.autoescola.util.ValidacaoUtil.camposInvalidos;
import static org.assertj.core.api.Assertions.assertThat;

class DadosAgendamentoInstrucaoValidacaoTest {

    @Test
    @DisplayName("Agendamento válido, com instrutor ou com especialidade")
    void valido() {
        var futuro = LocalDateTime.now().plusDays(2);

        assertThat(camposInvalidos(new DadosAgendamentoInstrucao(1L, 2L, null, futuro))).isEmpty();
        assertThat(camposInvalidos(new DadosAgendamentoInstrucao(1L, null, Especialidade.CARROS, futuro))).isEmpty();
    }

    @Test
    @DisplayName("Aluno e data são obrigatórios")
    void obrigatorios() {
        assertThat(camposInvalidos(new DadosAgendamentoInstrucao(null, null, null, null)))
                .containsExactlyInAnyOrder("idAluno", "dataHora");
    }

    @Test
    @DisplayName("Data no passado é rejeitada")
    void dataNoPassado() {
        var passado = LocalDateTime.now().minusDays(1);

        assertThat(camposInvalidos(new DadosAgendamentoInstrucao(1L, 2L, null, passado)))
                .containsExactly("dataHora");
    }
}
