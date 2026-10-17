package br.com.fiap3espg.autoescola.domain.endereco;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnderecoTest {

    private DadosEndereco dados() {
        return new DadosEndereco("Rua A", "10", "Casa", "Centro", "Campinas", "SP", "13000-000");
    }

    @Test
    @DisplayName("Construtor copia todos os campos do DTO")
    void construtor() {
        Endereco e = new Endereco(dados());

        assertThat(e.getLogradouro()).isEqualTo("Rua A");
        assertThat(e.getNumero()).isEqualTo("10");
        assertThat(e.getComplemento()).isEqualTo("Casa");
        assertThat(e.getBairro()).isEqualTo("Centro");
        assertThat(e.getCidade()).isEqualTo("Campinas");
        assertThat(e.getUf()).isEqualTo("SP");
        assertThat(e.getCep()).isEqualTo("13000-000");
    }

    @Test
    @DisplayName("Atualização parcial altera só os campos informados")
    void atualizacaoParcial() {
        Endereco e = new Endereco(dados());

        e.atualizarInformacoes(new DadosEndereco(null, null, null, null, "Santos", null, null));

        assertThat(e.getCidade()).isEqualTo("Santos");
        assertThat(e.getLogradouro()).isEqualTo("Rua A");
        assertThat(e.getUf()).isEqualTo("SP");
        assertThat(e.getCep()).isEqualTo("13000-000");
    }

    @Test
    @DisplayName("Atualização ignora campos obrigatórios em branco")
    void ignoraEmBranco() {
        Endereco e = new Endereco(dados());

        e.atualizarInformacoes(new DadosEndereco(" ", null, null, "", " ", "", " "));

        assertThat(e.getLogradouro()).isEqualTo("Rua A");
        assertThat(e.getBairro()).isEqualTo("Centro");
        assertThat(e.getCidade()).isEqualTo("Campinas");
        assertThat(e.getUf()).isEqualTo("SP");
        assertThat(e.getCep()).isEqualTo("13000-000");
    }

    @Test
    @DisplayName("Número e complemento podem ser alterados")
    void numeroEComplemento() {
        Endereco e = new Endereco(dados());

        e.atualizarInformacoes(new DadosEndereco(null, "99", "Fundos", null, null, null, null));

        assertThat(e.getNumero()).isEqualTo("99");
        assertThat(e.getComplemento()).isEqualTo("Fundos");
    }

    @Test
    @DisplayName("DadosEndereco(Endereco) faz o caminho inverso sem perder dados")
    void roundTrip() {
        DadosEndereco original = dados();

        assertThat(new DadosEndereco(new Endereco(original))).isEqualTo(original);
    }
}
