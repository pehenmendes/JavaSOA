package br.com.fiap3espg.autoescola.service;

import br.com.fiap3espg.autoescola.domain.aluno.Aluno;
import br.com.fiap3espg.autoescola.domain.aluno.AlunoRepository;
import br.com.fiap3espg.autoescola.domain.instrucao.*;
import br.com.fiap3espg.autoescola.domain.instrucao.validacao.ValidadorAgendamento;
import br.com.fiap3espg.autoescola.domain.instrutor.Especialidade;
import br.com.fiap3espg.autoescola.domain.instrutor.Instrutor;
import br.com.fiap3espg.autoescola.domain.instrutor.InstrutorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstrucaoServiceTest {
    @Mock InstrucaoRepository repository;
    @Mock AlunoRepository alunoRepository;
    @Mock InstrutorRepository instrutorRepository;
    @Mock ValidadorAgendamento validador;

    private InstrucaoService service() {
        return new InstrucaoService(repository, alunoRepository, instrutorRepository, List.of(validador));
    }

    @Test
    @DisplayName("Sem instrutor e sem especialidade: rejeita antes de qualquer consulta de escolha")
    void semInstrutorSemEspecialidade() {
        when(alunoRepository.existsById(1L)).thenReturn(true);
        var dados = new DadosAgendamentoInstrucao(1L, null, null, LocalDateTime.now().plusDays(1));

        assertThatThrownBy(() -> service().agendarInstrucao(dados)).isInstanceOf(ValidacaoException.class);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Sem instrutor disponível: rejeita e não grava")
    void semInstrutorDisponivel() {
        when(alunoRepository.existsById(1L)).thenReturn(true);
        when(instrutorRepository.escolherInstrutorAleatorioDisponivel(any(), any())).thenReturn(null);
        var dados = new DadosAgendamentoInstrucao(1L, null, Especialidade.CARROS, LocalDateTime.now().plusDays(1));

        assertThatThrownBy(() -> service().agendarInstrucao(dados)).isInstanceOf(ValidacaoException.class);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Cancelar instrução inexistente lança InstrucaoNotFoundException")
    void cancelarInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service().cancelarInstrucao(99L)).isInstanceOf(InstrucaoNotFoundException.class);
    }

    @Test
    @DisplayName("Cancelar instrução marca como inativa")
    void cancelar() {
        var instrucao = new Instrucao(1L, mock(Aluno.class), mock(Instrutor.class), LocalDateTime.now(), true);
        when(repository.findById(1L)).thenReturn(Optional.of(instrucao));

        service().cancelarInstrucao(1L);

        org.assertj.core.api.Assertions.assertThat(instrucao.isAtivo()).isFalse();
    }
}
