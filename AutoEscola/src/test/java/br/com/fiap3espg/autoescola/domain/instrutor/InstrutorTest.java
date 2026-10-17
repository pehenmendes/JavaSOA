package br.com.fiap3espg.autoescola.domain.instrutor;

import br.com.fiap3espg.autoescola.domain.endereco.DadosEndereco;
import br.com.fiap3espg.autoescola.util.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InstrutorTest {

    @Test
    @DisplayName("Construtor a partir do DTO cria instrutor ativo")
    void construtor() {
        var dados = new DadosCadastroInstrutor(
                "João", "joao@email.com", "(11) 98888-7777", "12345678901",
                Especialidade.CAMINHOES, Fixtures.dadosEndereco());

        Instrutor instrutor = new Instrutor(dados);

        assertThat(instrutor.getNome()).isEqualTo("João");
        assertThat(instrutor.getEmail()).isEqualTo("joao@email.com");
        assertThat(instrutor.getTelefone()).isEqualTo("(11) 98888-7777");
        assertThat(instrutor.getCnh()).isEqualTo("12345678901");
        assertThat(instrutor.getEspecialidade()).isEqualTo(Especialidade.CAMINHOES);
        assertThat(instrutor.getEndereco().getCep()).isEqualTo("01234-000");
        assertThat(instrutor.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Atualização altera os campos informados")
    void atualizacao() {
        Instrutor instrutor = Fixtures.instrutor(1, Especialidade.CARROS);

        instrutor.atualizarInformacoes(new DadosAtualizacaoInstrutor(
                1L, "Novo", "novo@email.com", "(11) 90000-1111", Especialidade.MOTOS, null));

        assertThat(instrutor.getNome()).isEqualTo("Novo");
        assertThat(instrutor.getEmail()).isEqualTo("novo@email.com");
        assertThat(instrutor.getTelefone()).isEqualTo("(11) 90000-1111");
        assertThat(instrutor.getEspecialidade()).isEqualTo(Especialidade.MOTOS);
    }

    @Test
    @DisplayName("Atualização com nulos ou brancos mantém os valores atuais")
    void ignoraNulosEBrancos() {
        Instrutor instrutor = Fixtures.instrutor(1, Especialidade.CARROS);

        instrutor.atualizarInformacoes(new DadosAtualizacaoInstrutor(1L, "", " ", null, null, null));

        assertThat(instrutor.getNome()).isEqualTo("Instrutor 1");
        assertThat(instrutor.getEmail()).isEqualTo("instrutor1@email.com");
        assertThat(instrutor.getEspecialidade()).isEqualTo(Especialidade.CARROS);
    }

    @Test
    @DisplayName("Atualização nunca altera a CNH")
    void cnhImutavel() {
        Instrutor instrutor = Fixtures.instrutor(1, Especialidade.CARROS);

        instrutor.atualizarInformacoes(new DadosAtualizacaoInstrutor(1L, "X", null, null, null, null));

        assertThat(instrutor.getCnh()).isEqualTo("00000000001");
    }

    @Test
    @DisplayName("Atualização repassa o endereço parcial")
    void atualizaEndereco() {
        Instrutor instrutor = Fixtures.instrutor(1, Especialidade.VANS);

        instrutor.atualizarInformacoes(new DadosAtualizacaoInstrutor(
                1L, null, null, null, null,
                new DadosEndereco(null, null, null, "Moema", null, null, null)));

        assertThat(instrutor.getEndereco().getBairro()).isEqualTo("Moema");
        assertThat(instrutor.getEndereco().getCidade()).isEqualTo("São Paulo");
    }

    @Test
    @DisplayName("Excluir faz exclusão lógica")
    void excluir() {
        Instrutor instrutor = Fixtures.instrutor(1, Especialidade.CARROS);

        instrutor.excluir();

        assertThat(instrutor.isAtivo()).isFalse();
    }

    @Test
    @DisplayName("Igualdade considera apenas o id")
    void igualdadePorId() {
        Instrutor a = Fixtures.instrutorComId(1L, 1, Especialidade.CARROS);
        Instrutor b = Fixtures.instrutorComId(1L, 2, Especialidade.MOTOS);
        Instrutor c = Fixtures.instrutorComId(2L, 1, Especialidade.CARROS);

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b).isNotEqualTo(c);
    }

    @Test
    @DisplayName("DTO de detalhamento coloca e-mail e telefone nos campos corretos")
    void detalhamento() {
        var dto = new DadosDetalhamentoInstrutor(Fixtures.instrutorComId(5L, 4, Especialidade.VANS));

        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.nome()).isEqualTo("Instrutor 4");
        assertThat(dto.email()).isEqualTo("instrutor4@email.com");
        assertThat(dto.telefone()).isEqualTo("(11) 98765-4321");
        assertThat(dto.cnh()).isEqualTo("00000000004");
        assertThat(dto.especialidade()).isEqualTo(Especialidade.VANS);
        assertThat(dto.ativo()).isTrue();
    }

    @Test
    @DisplayName("DTO de listagem reflete a entidade")
    void listagem() {
        var dto = new DadosListagemInstrutor(Fixtures.instrutorComId(5L, 4, Especialidade.MOTOS));

        assertThat(dto.id()).isEqualTo(5L);
        assertThat(dto.nome()).isEqualTo("Instrutor 4");
        assertThat(dto.especialidade()).isEqualTo(Especialidade.MOTOS);
    }
}
