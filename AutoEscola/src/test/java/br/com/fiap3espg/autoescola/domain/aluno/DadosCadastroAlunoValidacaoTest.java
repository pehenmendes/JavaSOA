package br.com.fiap3espg.autoescola.domain.aluno;

import br.com.fiap3espg.autoescola.domain.endereco.DadosEndereco;
import br.com.fiap3espg.autoescola.util.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static br.com.fiap3espg.autoescola.util.ValidacaoUtil.camposInvalidos;
import static org.assertj.core.api.Assertions.assertThat;

class DadosCadastroAlunoValidacaoTest {

    private DadosCadastroAluno valido() {
        return new DadosCadastroAluno("Maria", "maria@email.com", "(11) 99999-0000",
                "12345678901", "00001", TipoCarta.CAT_B, Fixtures.dadosEndereco());
    }

    @Test
    @DisplayName("DTO válido não tem violações")
    void valiar() {
        assertThat(camposInvalidos(valido())).isEmpty();
    }

    @Test
    @DisplayName("Todos os campos nulos geram violação")
    void tudoNulo() {
        var dto = new DadosCadastroAluno(null, null, null, null, null, null, null);

        assertThat(camposInvalidos(dto))
                .containsExactlyInAnyOrder("nome", "email", "telefone", "cpf", "matricula", "tipoCarta", "endereco");
    }

    @Test
    @DisplayName("Strings em branco são rejeitadas")
    void emBranco() {
        var dto = new DadosCadastroAluno(" ", " ", " ", "12345678901", "00001", TipoCarta.CAT_B,
                Fixtures.dadosEndereco());

        assertThat(camposInvalidos(dto)).contains("nome", "email", "telefone");
    }

    @ParameterizedTest
    @ValueSource(strings = {"maria", "maria@", "@email.com", "maria email.com"})
    @DisplayName("E-mail mal formatado é rejeitado")
    void emailInvalido(String email) {
        var dto = new DadosCadastroAluno("Maria", email, "1", "12345678901", "00001", TipoCarta.CAT_B,
                Fixtures.dadosEndereco());

        assertThat(camposInvalidos(dto)).contains("email");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567890", "123456789012", "1234567890a", "123.456.789-01", ""})
    @DisplayName("CPF precisa ter exatamente 11 dígitos")
    void cpfInvalido(String cpf) {
        var dto = new DadosCadastroAluno("Maria", "m@email.com", "1", cpf, "00001", TipoCarta.CAT_B,
                Fixtures.dadosEndereco());

        assertThat(camposInvalidos(dto)).contains("cpf");
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234", "123456", "abcde", ""})
    @DisplayName("Matrícula precisa ter exatamente 5 dígitos")
    void matriculaInvalida(String matricula) {
        var dto = new DadosCadastroAluno("Maria", "m@email.com", "1", "12345678901", matricula,
                TipoCarta.CAT_B, Fixtures.dadosEndereco());

        assertThat(camposInvalidos(dto)).contains("matricula");
    }

    @Test
    @DisplayName("Endereço é validado em cascata")
    void enderecoEmCascata() {
        var endereco = new DadosEndereco("", null, null, "", "", "sp", "123");
        var dto = new DadosCadastroAluno("Maria", "m@email.com", "1", "12345678901", "00001",
                TipoCarta.CAT_B, endereco);

        assertThat(camposInvalidos(dto))
                .contains("endereco.logradouro", "endereco.bairro", "endereco.cidade", "endereco.uf", "endereco.cep");
    }
}
