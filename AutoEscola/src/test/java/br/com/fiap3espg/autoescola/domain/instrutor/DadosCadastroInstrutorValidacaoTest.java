package br.com.fiap3espg.autoescola.domain.instrutor;

import br.com.fiap3espg.autoescola.util.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static br.com.fiap3espg.autoescola.util.ValidacaoUtil.camposInvalidos;
import static org.assertj.core.api.Assertions.assertThat;

class DadosCadastroInstrutorValidacaoTest {

    private DadosCadastroInstrutor comCnh(String cnh) {
        return new DadosCadastroInstrutor("João", "joao@email.com", "(11) 98888-7777", cnh,
                Especialidade.CARROS, Fixtures.dadosEndereco());
    }

    @Test
    @DisplayName("DTO válido não tem violações")
    void valido() {
        assertThat(camposInvalidos(comCnh("12345678901"))).isEmpty();
    }

    @Test
    @DisplayName("Todos os campos nulos geram violação")
    void tudoNulo() {
        var dto = new DadosCadastroInstrutor(null, null, null, null, null, null);

        assertThat(camposInvalidos(dto))
                .containsExactlyInAnyOrder("nome", "email", "telefone", "cnh", "especialidade", "endereco");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "123456789012", "abcdefghijk", ""})
    @DisplayName("CNH precisa ter exatamente 11 dígitos")
    void cnhInvalida(String cnh) {
        assertThat(camposInvalidos(comCnh(cnh))).contains("cnh");
    }

    @Test
    @DisplayName("E-mail inválido é rejeitado")
    void emailInvalido() {
        var dto = new DadosCadastroInstrutor("João", "joao", "1", "12345678901",
                Especialidade.MOTOS, Fixtures.dadosEndereco());

        assertThat(camposInvalidos(dto)).containsExactly("email");
    }
}
