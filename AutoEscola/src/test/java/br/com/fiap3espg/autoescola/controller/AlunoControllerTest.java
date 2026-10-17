package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.domain.aluno.*;
import br.com.fiap3espg.autoescola.service.AlunoService;
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
class AlunoControllerTest {
    private static final String ALUNO_VALIDO = """
            {
              "nome": "Maria",
              "email": "maria@email.com",
              "telefone": "(11) 91234-5678",
              "cpf": "12345678901",
              "matricula": "12345",
              "tipoCarta": "CAT_B",
              "endereco": {
                "logradouro": "Rua A", "numero": "10", "complemento": "",
                "bairro": "Centro", "cidade": "Sao Paulo", "uf": "SP", "cep": "01234-000"
              }
            }
            """;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AlunoService service;

    @Test
    @DisplayName("POST /alunos como ADMIN com dados válidos retorna 201, Location e corpo")
    @WithMockUser(roles = "ADMIN")
    void cadastrar() throws Exception {
        when(service.cadastrarAluno(any())).thenReturn(new DadosDetalhamentoAluno(Fixtures.alunoComId(1L, 1)));

        mockMvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON).content(ALUNO_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/alunos/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Aluno 1"));
    }

    @Test
    @DisplayName("POST /alunos com corpo vazio retorna 400 listando os campos inválidos")
    @WithMockUser(roles = "ADMIN")
    void cadastrarInvalido() throws Exception {
        mockMvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@.field=='nome')]").exists())
                .andExpect(jsonPath("$[?(@.field=='cpf')]").exists())
                .andExpect(jsonPath("$[?(@.field=='matricula')]").exists());

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("POST /alunos com CPF de 9 dígitos retorna 400")
    @WithMockUser(roles = "ADMIN")
    void cadastrarCpfCurto() throws Exception {
        mockMvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON)
                        .content(ALUNO_VALIDO.replace("12345678901", "123456789")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@.field=='cpf')]").exists());
    }

    @Test
    @DisplayName("POST /alunos como USER retorna 403")
    @WithMockUser(roles = "USER")
    void cadastrarSemPermissao() throws Exception {
        mockMvc.perform(post("/alunos").contentType(MediaType.APPLICATION_JSON).content(ALUNO_VALIDO))
                .andExpect(status().isForbidden());

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("Sem autenticação a requisição é rejeitada")
    void semAutenticacao() throws Exception {
        mockMvc.perform(get("/alunos")).andExpect(status().is4xxClientError());

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("GET /alunos como ADMIN retorna a página de alunos")
    @WithMockUser(roles = "ADMIN")
    void listar() throws Exception {
        var pagina = new PageImpl<>(List.of(new DadosListagemAluno(Fixtures.alunoComId(1L, 1))));
        when(service.listarAlunos(any())).thenReturn(pagina);

        mockMvc.perform(get("/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Aluno 1"))
                .andExpect(jsonPath("$.content[0].matricula").value("00001"));
    }

    @Test
    @DisplayName("GET /alunos/{id} inexistente retorna 404 com mensagem")
    @WithMockUser(roles = "ADMIN")
    void detalharInexistente() throws Exception {
        when(service.detalharAluno(99L)).thenThrow(new AlunoNotFoundException("ID do aluno não existe!"));

        mockMvc.perform(get("/alunos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ID do aluno não existe!"));
    }

    @Test
    @DisplayName("PUT /alunos sem id retorna 400")
    @WithMockUser(roles = "ADMIN")
    void atualizarSemId() throws Exception {
        mockMvc.perform(put("/alunos").contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"Novo\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@.field=='id')]").exists());
    }

    @Test
    @DisplayName("PUT /alunos com id retorna 200 e os dados atualizados")
    @WithMockUser(roles = "ADMIN")
    void atualizar() throws Exception {
        when(service.atualizarAluno(any())).thenReturn(new DadosDetalhamentoAluno(Fixtures.alunoComId(1L, 1)));

        mockMvc.perform(put("/alunos").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"nome\":\"Aluno 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @DisplayName("DELETE /alunos/{id} retorna 204 e delega ao serviço")
    @WithMockUser(roles = "ADMIN")
    void excluir() throws Exception {
        mockMvc.perform(delete("/alunos/1")).andExpect(status().isNoContent());

        verify(service).excluirAluno(1L);
    }
}
