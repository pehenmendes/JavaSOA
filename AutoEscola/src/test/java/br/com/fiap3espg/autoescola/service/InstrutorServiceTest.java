package br.com.fiap3espg.autoescola.service;

import br.com.fiap3espg.autoescola.domain.instrutor.*;
import br.com.fiap3espg.autoescola.util.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstrutorServiceTest {
    @Mock
    InstrutorRepository repository;

    @InjectMocks
    InstrutorService service;

    @Test
    @DisplayName("Cadastrar salva o instrutor e devolve o detalhamento")
    void cadastrar() {
        var dados = new DadosCadastroInstrutor(
                "Carlos", "carlos@email.com", "(11) 98765-4321", "12345678901",
                Especialidade.CARROS, Fixtures.dadosEndereco());
        when(repository.save(any(Instrutor.class))).thenAnswer(i -> {
            Instrutor x = i.getArgument(0);
            return new Instrutor(7L, x.getNome(), x.getEmail(), x.getTelefone(), x.getCnh(),
                    x.getEspecialidade(), x.getEndereco(), x.isAtivo());
        });

        var resposta = service.cadastrarInstrutor(dados);

        assertThat(resposta.id()).isEqualTo(7L);
        assertThat(resposta.especialidade()).isEqualTo(Especialidade.CARROS);
        assertThat(resposta.ativo()).isTrue();
    }

    @Test
    @DisplayName("Listar consulta somente instrutores ativos")
    void listar() {
        Pageable paginacao = PageRequest.of(0, 5);
        Page<Instrutor> pagina = new PageImpl<>(List.of(
                Fixtures.instrutorComId(1L, 1, Especialidade.MOTOS),
                Fixtures.instrutorComId(2L, 2, Especialidade.VANS)));
        when(repository.findAllByAtivoTrue(paginacao)).thenReturn(pagina);

        var resposta = service.listarInstrutores(paginacao);

        assertThat(resposta.getContent()).extracting(DadosListagemInstrutor::especialidade)
                .containsExactly(Especialidade.MOTOS, Especialidade.VANS);
    }

    @Test
    @DisplayName("Detalhar instrutor inexistente lança InstrutorNotFoundException")
    void detalharInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.detalharInstrutor(99L)).isInstanceOf(InstrutorNotFoundException.class);
    }

    @Test
    @DisplayName("Instrutor inativo é tratado como inexistente")
    void detalharInativo() {
        Instrutor inativo = Fixtures.instrutorComId(1L, 1, Especialidade.CARROS);
        inativo.excluir();
        when(repository.findById(1L)).thenReturn(Optional.of(inativo));

        assertThatThrownBy(() -> service.detalharInstrutor(1L)).isInstanceOf(InstrutorNotFoundException.class);
    }

    @Test
    @DisplayName("Atualizar altera a especialidade e mantém o restante")
    void atualizar() {
        Instrutor existente = Fixtures.instrutorComId(1L, 1, Especialidade.CARROS);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);
        var dados = new DadosAtualizacaoInstrutor(1L, null, null, null, Especialidade.CAMINHOES, null);

        var resposta = service.atualizarInstrutor(dados);

        assertThat(resposta.especialidade()).isEqualTo(Especialidade.CAMINHOES);
        assertThat(resposta.nome()).isEqualTo("Instrutor 1");
    }

    @Test
    @DisplayName("Excluir faz exclusão lógica")
    void excluir() {
        Instrutor existente = Fixtures.instrutorComId(1L, 1, Especialidade.CARROS);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        service.deleteInstrutor(1L);

        assertThat(existente.isAtivo()).isFalse();
        verify(repository).save(existente);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Excluir instrutor inexistente lança exceção")
    void excluirInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteInstrutor(99L)).isInstanceOf(InstrutorNotFoundException.class);
        verify(repository, never()).save(any());
    }
}
