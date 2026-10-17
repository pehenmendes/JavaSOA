package br.com.fiap3espg.autoescola.controller;

import br.com.fiap3espg.autoescola.domain.usuario.*;
import br.com.fiap3espg.autoescola.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService service;

    private DadosDetalhamentoUsuario usuario() {
        return new DadosDetalhamentoUsuario(1L, "maria", Role.USER, true);
    }

    @Test
    @DisplayName("POST /usuarios como ADMIN retorna 201 e nunca expõe a senha")
    @WithMockUser(roles = "ADMIN")
    void cadastrar() throws Exception {
        when(service.cadastrarUsuario(any())).thenReturn(usuario());

        mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"maria\",\"senha\":\"segredo123\",\"perfil\":\"USER\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/usuarios/1")))
                .andExpect(jsonPath("$.login").value("maria"))
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @DisplayName("POST /usuarios com senha curta ou login em branco retorna 400")
    @WithMockUser(roles = "ADMIN")
    void cadastrarInvalido() throws Exception {
        mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"\",\"senha\":\"123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@.field=='login')]").exists())
                .andExpect(jsonPath("$[?(@.field=='senha')]").exists());

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("POST /usuarios como USER retorna 403")
    @WithMockUser(roles = "USER")
    void cadastrarSemPermissao() throws Exception {
        mockMvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"maria\",\"senha\":\"segredo123\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /usuarios como USER retorna 403")
    @WithMockUser(roles = "USER")
    void listarSemPermissao() throws Exception {
        mockMvc.perform(get("/usuarios")).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("GET /usuarios/{id} inexistente retorna 404 com mensagem")
    @WithMockUser(roles = "ADMIN")
    void detalharInexistente() throws Exception {
        when(service.detalharUsuario(99L)).thenThrow(new UsuarioNotFoundException("ID do usuário não existe"));

        mockMvc.perform(get("/usuarios/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ID do usuário não existe"));
    }

    @Test
    @DisplayName("PATCH /usuarios/{id}/senha como USER usa o id do caminho e retorna 200")
    @WithMockUser(roles = "USER")
    void trocarSenha() throws Exception {
        when(service.atualizarSenha(eq(1L), any(), any(Authentication.class))).thenReturn(usuario());

        mockMvc.perform(patch("/usuarios/1/senha").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senhaAtual\":\"antiga123\",\"senhaNova\":\"nova12345\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.senha").doesNotExist());

        verify(service).atualizarSenha(eq(1L), eq(new DadosAtualizarSenhaUsuario("antiga123", "nova12345")),
                any(Authentication.class));
    }

    @Test
    @DisplayName("PATCH senha sem a senha atual retorna 400")
    @WithMockUser(roles = "USER")
    void trocarSenhaSemSenhaAtual() throws Exception {
        mockMvc.perform(patch("/usuarios/1/senha").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senhaNova\":\"nova12345\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[?(@.field=='senhaAtual')]").exists());

        verifyNoInteractions(service);
    }

    @Test
    @DisplayName("PATCH senha com senha atual incorreta retorna 400 com mensagem")
    @WithMockUser(roles = "USER")
    void trocarSenhaIncorreta() throws Exception {
        when(service.atualizarSenha(eq(1L), any(), any()))
                .thenThrow(new SenhaAtualIncorretaException("Senha atual incorreta"));

        mockMvc.perform(patch("/usuarios/1/senha").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senhaAtual\":\"errada123\",\"senhaNova\":\"nova12345\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Senha atual incorreta"));
    }

    @Test
    @DisplayName("PATCH senha de outro usuário (AccessDenied no serviço) retorna 403")
    @WithMockUser(roles = "USER")
    void trocarSenhaDeOutro() throws Exception {
        when(service.atualizarSenha(eq(2L), any(), any()))
                .thenThrow(new AccessDeniedException("Você só pode alterar sua própria senha"));

        mockMvc.perform(patch("/usuarios/2/senha").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"senhaAtual\":\"antiga123\",\"senhaNova\":\"nova12345\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /usuarios/{id} como ADMIN retorna 204")
    @WithMockUser(roles = "ADMIN")
    void excluir() throws Exception {
        mockMvc.perform(delete("/usuarios/1")).andExpect(status().isNoContent());

        verify(service).excluirUsuario(1L);
    }
}
