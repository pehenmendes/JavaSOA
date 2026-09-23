package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.domain.instrucao.DadosAgendamentoInstrucao;
import br.com.fiap3espg.autoescola.domain.instrucao.DadosDetalhamentoAgendamento;
import br.com.fiap3espg.autoescola.service.InstrucaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
}
