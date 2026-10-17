package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.domain.instrucao.DadosDetalhamentoAgendamento;
import br.com.fiap3espg.autoescola.service.InstrucaoService;
import br.com.fiap3espg.autoescola.domain.aluno.AlunoNotFoundException;
import br.com.fiap3espg.autoescola.domain.instrucao.Instrucao;
import br.com.fiap3espg.autoescola.domain.instrucao.InstrucaoNotFoundException;
import br.com.fiap3espg.autoescola.domain.instrucao.ValidacaoException;
import br.com.fiap3espg.autoescola.domain.instrutor.Especialidade;
import br.com.fiap3espg.autoescola.util.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class InstrucaoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    JacksonTester<DadosAgendamentoInstrucao> dadosAgendamentoJson;

    @Autowired
    JacksonTester<DadosDetalhamentoAgendamento> dadosDetalhamentoJson;

    @MockitoBean
    InstrucaoService agenda;

    @Test
    @DisplayName("Expectativa: retornar codigo 400 para informações inválidas")
    @WithMockUser
    void agendarInstrucaoCenario1() throws Exception{
        MockHttpServletResponse response = mockMvc
                .perform(post("/instrucoes"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static final String AGENDAMENTO_VALIDO = """
            {"id_aluno": 1, "id_instrutor": 1, "data_hora": "15/12/2030 - 10:00"}
            """;

    private DadosDetalhamentoAgendamento agendamento() {
        var instrucao = new Instrucao(
                1L,
                Fixtures.alunoComId(1L, 1),
                Fixtures.instrutorComId(1L, 1, Especialidade.CARROS),
                LocalDateTime.of(2030, 12, 15, 10, 0),
                true);
        return new DadosDetalhamentoAgendamento(instrucao);
    }

    @Test
    @DisplayName("POST /instrucoes como ADMIN com dados válidos retorna 200 e o agendamento")
    @WithMockUser(roles = "ADMIN")
    void agendarComSucesso() throws Exception {
        when(agenda.agendarInstrucao(any())).thenReturn(agendamento());

        mockMvc.perform(post("/instrucoes").contentType(MediaType.APPLICATION_JSON).content(AGENDAMENTO_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeAluno").value("Aluno 1"))
                .andExpect(jsonPath("$.nomeInstrutor").value("Instrutor 1"))
                .andExpect(jsonPath("$.especialidade").value("CARROS"))
                .andExpect(jsonPath("$.dataHora").value("15/12/2030 - 10:00"));
    }

    @Test
    @DisplayName("POST /instrucoes sem id_aluno retorna 400")
    @WithMockUser(roles = "ADMIN")
    void agendarSemAluno() throws Exception {
        mockMvc.perform(post("/instrucoes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"data_hora\": \"15/12/2030 - 10:00\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(agenda);
    }

    @Test
    @DisplayName("POST /instrucoes com data no passado retorna 400")
    @WithMockUser(roles = "ADMIN")
    void agendarNoPassado() throws Exception {
        mockMvc.perform(post("/instrucoes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id_aluno\": 1, \"data_hora\": \"01/01/2020 - 10:00\"}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(agenda);
    }

    @Test
    @DisplayName("POST /instrucoes como USER retorna 403")
    @WithMockUser(roles = "USER")
    void agendarSemPermissao() throws Exception {
        mockMvc.perform(post("/instrucoes").contentType(MediaType.APPLICATION_JSON).content(AGENDAMENTO_VALIDO))
                .andExpect(status().isForbidden());

        verifyNoInteractions(agenda);
    }

    @Test
    @DisplayName("Violação de regra de negócio no agendamento retorna 400 com a mensagem")
    @WithMockUser(roles = "ADMIN")
    void agendarRegraDeNegocio() throws Exception {
        when(agenda.agendarInstrucao(any())).thenThrow(new ValidacaoException("Instrutor ocupado na data/hora escolhida"));

        mockMvc.perform(post("/instrucoes").contentType(MediaType.APPLICATION_JSON).content(AGENDAMENTO_VALIDO))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Instrutor ocupado na data/hora escolhida"));
    }

    @Test
    @DisplayName("Aluno inexistente no agendamento retorna 404")
    @WithMockUser(roles = "ADMIN")
    void agendarAlunoInexistente() throws Exception {
        when(agenda.agendarInstrucao(any())).thenThrow(new AlunoNotFoundException("ID do aluno informado não existe"));

        mockMvc.perform(post("/instrucoes").contentType(MediaType.APPLICATION_JSON).content(AGENDAMENTO_VALIDO))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /instrucoes/{id} retorna 204 e cancela")
    @WithMockUser(roles = "ADMIN")
    void cancelar() throws Exception {
        mockMvc.perform(delete("/instrucoes/1")).andExpect(status().isNoContent());

        verify(agenda).cancelarInstrucao(1L);
    }

    @Test
    @DisplayName("DELETE /instrucoes/{id} inexistente retorna 404")
    @WithMockUser(roles = "ADMIN")
    void cancelarInexistente() throws Exception {
        org.mockito.Mockito.doThrow(new InstrucaoNotFoundException("ID do agendamento não existe"))
                .when(agenda).cancelarInstrucao(99L);

        mockMvc.perform(delete("/instrucoes/99")).andExpect(status().isNotFound());
    }
}
