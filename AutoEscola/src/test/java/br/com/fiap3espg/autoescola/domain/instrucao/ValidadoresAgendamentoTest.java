package br.com.fiap3espg.autoescola.domain.instrucao;

import br.com.fiap3espg.autoescola.domain.instrucao.validacao.*;
import br.com.fiap3espg.autoescola.domain.instrutor.InstrutorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidadoresAgendamentoTest {
    @Mock
    InstrucaoRepository instrucaoRepository;

    @Mock
    InstrutorRepository instrutorRepository;

    private static LocalDateTime proximo(DayOfWeek dia, int hora) {
        return LocalDateTime.now()
                .with(TemporalAdjusters.next(dia))
                .withHour(hora).withMinute(0).withSecond(0).withNano(0);
    }

    private static DadosAgendamentoInstrucao dados(Long idInstrutor, LocalDateTime dataHora) {
        return new DadosAgendamentoInstrucao(1L, idInstrutor, null, dataHora);
    }

    @Test
    @DisplayName("Funcionamento: aceita segunda 6h e sábado 20h")
    void funcionamentoAceita() {
        var v = new ValidadorHorarioFuncionamento();
        assertThatCode(() -> v.validar(dados(1L, proximo(DayOfWeek.MONDAY, 6)))).doesNotThrowAnyException();
        assertThatCode(() -> v.validar(dados(1L, proximo(DayOfWeek.SATURDAY, 20)))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Funcionamento: rejeita domingo, 5h e 21h")
    void funcionamentoRejeita() {
        var v = new ValidadorHorarioFuncionamento();
        assertThatThrownBy(() -> v.validar(dados(1L, proximo(DayOfWeek.SUNDAY, 10)))).isInstanceOf(ValidacaoException.class);
        assertThatThrownBy(() -> v.validar(dados(1L, proximo(DayOfWeek.MONDAY, 5)))).isInstanceOf(ValidacaoException.class);
        assertThatThrownBy(() -> v.validar(dados(1L, proximo(DayOfWeek.MONDAY, 21)))).isInstanceOf(ValidacaoException.class);
    }

    @Test
    @DisplayName("Hora inteira: rejeita minutos diferentes de zero")
    void horaInteira() {
        var v = new ValidadorHoraInteira();
        assertThatThrownBy(() -> v.validar(dados(1L, proximo(DayOfWeek.MONDAY, 10).withMinute(30))))
                .isInstanceOf(ValidacaoException.class);
    }

    @Test
    @DisplayName("Antecedência: rejeita menos de 30 minutos")
    void antecedencia() {
        var v = new ValidadorHorarioAntecedencia();
        assertThatThrownBy(() -> v.validar(dados(1L, LocalDateTime.now().plusMinutes(10))))
                .isInstanceOf(ValidacaoException.class);
        assertThatCode(() -> v.validar(dados(1L, LocalDateTime.now().plusHours(2)))).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Conflito de instrutor: ignora quando instrutor não foi informado")
    void conflitoSemInstrutor() {
        var v = new ValidadorConflitoHorarioInstrutor(instrucaoRepository);
        v.validar(dados(null, proximo(DayOfWeek.MONDAY, 10)));
        verifyNoInteractions(instrucaoRepository);
    }

    @Test
    @DisplayName("Conflito de instrutor: rejeita quando há instrução ativa no horário")
    void conflitoOcupado() {
        var v = new ValidadorConflitoHorarioInstrutor(instrucaoRepository);
        var hora = proximo(DayOfWeek.MONDAY, 10);
        when(instrucaoRepository.existsByInstrutorIdAndDataHoraAndAtivoTrue(5L, hora)).thenReturn(true);

        assertThatThrownBy(() -> v.validar(dados(5L, hora))).isInstanceOf(ValidacaoException.class);
    }

    @Test
    @DisplayName("Instrutor ativo: ignora quando instrutor não foi informado")
    void instrutorAtivoSemInstrutor() {
        var v = new ValidadorInstrutorAtivo(instrutorRepository);
        v.validar(dados(null, proximo(DayOfWeek.MONDAY, 10)));
        verifyNoInteractions(instrutorRepository);
    }

    @Test
    @DisplayName("Instrutor ativo: rejeita instrutor inativo")
    void instrutorInativo() {
        var v = new ValidadorInstrutorAtivo(instrutorRepository);
        when(instrutorRepository.existsByIdAndAtivoFalse(5L)).thenReturn(true);

        assertThatThrownBy(() -> v.validar(dados(5L, proximo(DayOfWeek.MONDAY, 10))))
                .isInstanceOf(ValidacaoException.class);
    }

    @Test
    @DisplayName("Limite diário: consulta o dia inteiro e rejeita segunda instrução")
    void limiteDiario() {
        var v = new ValidadorLimiteDiarioAluno(instrucaoRepository);
        var hora = proximo(DayOfWeek.MONDAY, 10);
        when(instrucaoRepository.existsByAlunoIdAndDataHoraBetweenAndAtivoTrue(anyLong(), any(), any()))
                .thenReturn(true);

        assertThatThrownBy(() -> v.validar(dados(5L, hora))).isInstanceOf(ValidacaoException.class);

        var inicio = ArgumentCaptor.forClass(LocalDateTime.class);
        var fim = ArgumentCaptor.forClass(LocalDateTime.class);
        verify(instrucaoRepository).existsByAlunoIdAndDataHoraBetweenAndAtivoTrue(
                anyLong(), inicio.capture(), fim.capture());
        assertThat(inicio.getValue()).isEqualTo(hora.toLocalDate().atStartOfDay());
        assertThat(fim.getValue().toLocalDate()).isEqualTo(hora.toLocalDate());
        assertThat(fim.getValue().getHour()).isEqualTo(23);
    }
}
