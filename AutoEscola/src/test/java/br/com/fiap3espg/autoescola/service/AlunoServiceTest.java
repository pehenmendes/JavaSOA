package br.com.fiap3espg.autoescola.service;

import br.com.fiap3espg.autoescola.domain.aluno.*;
import br.com.fiap3espg.autoescola.domain.endereco.DadosEndereco;
import br.com.fiap3espg.autoescola.util.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class AlunoServiceTest {
    @Mock
    AlunoRepository repository;

    @InjectMocks
    AlunoService service;

    @Test
    @DisplayName("Cadastrar salva o aluno e devolve o detalhamento")
    void cadastrar() {
        var dados = new DadosCadastroAluno(
                "Maria", "maria@email.com", "(11) 91234-5678", "12345678901", "12345",
                TipoCarta.CAT_B, Fixtures.dadosEndereco());
        when(repository.save(any(Aluno.class))).thenAnswer(i -> {
            Aluno a = i.getArgument(0);
            return new Aluno(10L, a.getNome(), a.getEmail(), a.getTelefone(), a.getCpf(),
                    a.getMatricula(), a.getTipoCarta(), a.getEndereco(), a.isAtivo());
        });

        var resposta = service.cadastrarAluno(dados);

        assertThat(resposta.id()).isEqualTo(10L);
        assertThat(resposta.nome()).isEqualTo("Maria");
        assertThat(resposta.cpf()).isEqualTo("12345678901");
        assertThat(resposta.ativo()).isTrue();

        ArgumentCaptor<Aluno> salvo = ArgumentCaptor.forClass(Aluno.class);
        verify(repository).save(salvo.capture());
        assertThat(salvo.getValue().getMatricula()).isEqualTo("12345");
    }

    @Test
    @DisplayName("Listar consulta somente alunos ativos e converte para DTO de listagem")
    void listar() {
        Pageable paginacao = PageRequest.of(0, 10);
        Page<Aluno> pagina = new PageImpl<>(List.of(Fixtures.alunoComId(1L, 1), Fixtures.alunoComId(2L, 2)));
        when(repository.findAllByAtivoTrue(paginacao)).thenReturn(pagina);

        Page<DadosListagemAluno> resposta = service.listarAlunos(paginacao);

        assertThat(resposta.getContent()).extracting(DadosListagemAluno::nome)
                .containsExactly("Aluno 1", "Aluno 2");
    }

    @Test
    @DisplayName("Detalhar devolve os dados do aluno ativo")
    void detalhar() {
        when(repository.findById(1L)).thenReturn(Optional.of(Fixtures.alunoComId(1L, 1)));

        var resposta = service.detalharAluno(1L);

        assertThat(resposta.id()).isEqualTo(1L);
        assertThat(resposta.email()).isEqualTo("aluno1@email.com");
    }

    @Test
    @DisplayName("Detalhar aluno inexistente lança AlunoNotFoundException")
    void detalharInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.detalharAluno(99L)).isInstanceOf(AlunoNotFoundException.class);
    }

    @Test
    @DisplayName("Aluno inativo é tratado como inexistente")
    void detalharInativo() {
        Aluno inativo = Fixtures.alunoComId(1L, 1);
        inativo.excluir();
        when(repository.findById(1L)).thenReturn(Optional.of(inativo));

        assertThatThrownBy(() -> service.detalharAluno(1L)).isInstanceOf(AlunoNotFoundException.class);
    }

    @Test
    @DisplayName("Atualizar altera apenas os campos informados e persiste")
    void atualizar() {
        Aluno existente = Fixtures.alunoComId(1L, 1);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);
        var dados = new DadosAtualizacaoAluno(1L, "Novo Nome", null, null, null, null);

        var resposta = service.atualizarAluno(dados);

        assertThat(resposta.nome()).isEqualTo("Novo Nome");
        assertThat(resposta.email()).isEqualTo("aluno1@email.com");
        verify(repository).save(existente);
    }

    @Test
    @DisplayName("Atualizar aluno inexistente lança exceção e não salva")
    void atualizarInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        var dados = new DadosAtualizacaoAluno(99L, "X", null, null, null, (DadosEndereco) null);

        assertThatThrownBy(() -> service.atualizarAluno(dados)).isInstanceOf(AlunoNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Excluir faz exclusão lógica")
    void excluir() {
        Aluno existente = Fixtures.alunoComId(1L, 1);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        service.excluirAluno(1L);

        assertThat(existente.isAtivo()).isFalse();
        verify(repository).save(existente);
        verify(repository, never()).delete(any());
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Excluir aluno já inativo lança exceção")
    void excluirInativo() {
        Aluno inativo = Fixtures.alunoComId(1L, 1);
        inativo.excluir();
        when(repository.findById(1L)).thenReturn(Optional.of(inativo));

        assertThatThrownBy(() -> service.excluirAluno(1L)).isInstanceOf(AlunoNotFoundException.class);
        verify(repository, never()).save(any());
    }
}
