package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.domain.instrutor.*;
import br.com.fiap3espg.autoescola.service.InstrutorService;
import br.com.fiap3espg.autoescola.util.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InstrutorControllerTest {
    private static final String INSTRUTOR_VALIDO = """
            {
              "nome": "Carlos",
              "email": "carlos@email.com",
              "telefone": "(11) 98765-4321",
              "cnh": "12345678901",
              "especialidade": "CARROS",
              "endereco": {
                "logradouro": "Rua B", "numero": "20", "complemento": "",
                "bairro": "Centro", "cidade": "Sao Paulo", "uf": "SP", "cep": "01234-000"
              }
            }
            """;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    InstrutorService service;

    private DadosDetalhamentoInstrutor instrutor() {
        return new DadosDetalhamentoInstrutor(Fixtures.instrutorComId(1L, 1, Especialidade.CARROS));
    }

    @Test
    @DisplayName("POST /instrutores como ADMIN retorna 201 e Location")
    @WithMockUser(roles = "ADMIN")
    void cadastrar() throws Exception {
        when(service.cadastrarInstrutor(any())).thenReturn(instrutor());

        mockMvc.perform(post("/instrutores").contentType(MediaType.APPLICATION_JSON).content(INSTRUTOR_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/instrutores/1")))
                .andExpect(jsonPath("$.especialidade").value("CARROS"));
    }

    @Test
    @DisplayName("POST /instrutores com especialidade inexistente retorna 400")
    @WithMockUser(roles = "ADMIN")
    void cadastrarEspecialidadeInvalida() throws Exception {
        mockMvc.perform(post("/instrutores").contentType(MediaType.APPLICATION_JSON)
                        .content(INSTRUTOR_VALIDO.replace("CARROS", "FOGUETES")))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("POST /instrutores com CNH curta ou e-mail inválido retorna 400 com os campos")
    @WithMockUser(roles = "ADMIN")
    void cadastrarInvalido() throws Exception {
        String corpo = INSTRUTOR_VALIDO.replace("12345678901", "123").replace("carlos@email.com", "sem-arroba");

        mockMvc.perform(post("/instrutores").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@.field=='cnh')]").exists())
                .andExpect(jsonPath("$[?(@.field=='email')]").exists());
    }

    @Test
    @DisplayName("POST /instrutores como USER retorna 403")
    @WithMockUser(roles = "USER")
    void cadastrarSemPermissao() throws Exception {
        mockMvc.perform(post("/instrutores").contentType(MediaType.APPLICATION_JSON).content(INSTRUTOR_VALIDO))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /instrutores é permitido para USER")
    @WithMockUser(roles = "USER")
    void listarComoUser() throws Exception {
        var pagina = new PageImpl<>(List.of(
                new DadosListagemInstrutor(Fixtures.instrutorComId(1L, 1, Especialidade.MOTOS))));
        when(service.listarInstrutores(any())).thenReturn(pagina);

        mockMvc.perform(get("/instrutores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].especialidade").value("MOTOS"));
    }

    @Test
    @DisplayName("GET /instrutores/{id} como USER retorna 403")
    @WithMockUser(roles = "USER")
    void detalharComoUser() throws Exception {
        mockMvc.perform(get("/instrutores/1")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /instrutores/{id} inexistente retorna 404 com mensagem")
    @WithMockUser(roles = "ADMIN")
    void detalharInexistente() throws Exception {
        when(service.detalharInstrutor(99L)).thenThrow(new InstrutorNotFoundException("ID do instrutor informado não existe!"));

        mockMvc.perform(get("/instrutores/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ID do instrutor informado não existe!"));
    }

    @Test
    @DisplayName("PUT /instrutores com id retorna 200")
    @WithMockUser(roles = "ADMIN")
    void atualizar() throws Exception {
        when(service.atualizarInstrutor(any())).thenReturn(instrutor());

        mockMvc.perform(put("/instrutores").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"especialidade\":\"CARROS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /instrutores/{id} retorna 204")
    @WithMockUser(roles = "ADMIN")
    void excluir() throws Exception {
        mockMvc.perform(delete("/instrutores/1")).andExpect(status().isNoContent());

        verify(service).deleteInstrutor(1L);
    }
}
