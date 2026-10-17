package br.com.fiap3espg.autoescola.domain.aluno;

import br.com.fiap3espg.autoescola.util.Fixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlunoTest {

    @Test
    @DisplayName("Construtor a partir do DTO cria aluno ativo com os dados informados")
    void construtor() {
        var dados = new DadosCadastroAluno(
                "Maria", "maria@email.com", "(11) 99999-0000", "12345678901", "00001",
                TipoCarta.CAT_AB, Fixtures.dadosEndereco());

        Aluno aluno = new Aluno(dados);

        assertThat(aluno.getId()).isNull();
        assertThat(aluno.getNome()).isEqualTo("Maria");
        assertThat(aluno.getCpf()).isEqualTo("12345678901");
        assertThat(aluno.getMatricula()).isEqualTo("00001");
        assertThat(aluno.getTipoCarta()).isEqualTo(TipoCarta.CAT_AB);
        assertThat(aluno.getEndereco().getCidade()).isEqualTo("São Paulo");
        assertThat(aluno.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("Atualização altera nome, e-mail, telefone e tipo de carta")
    void atualizacao() {
        Aluno aluno = Fixtures.aluno(1);

        aluno.atualizarInformacoes(new DadosAtualizacaoAluno(
                1L, "Novo Nome", "novo@email.com", "(21) 90000-0000", TipoCarta.CAT_D, null));

        assertThat(aluno.getNome()).isEqualTo("Novo Nome");
        assertThat(aluno.getEmail()).isEqualTo("novo@email.com");
        assertThat(aluno.getTelefone()).isEqualTo("(21) 90000-0000");
        assertThat(aluno.getTipoCarta()).isEqualTo(TipoCarta.CAT_D);
    }

    @Test
    @DisplayName("Atualização com campos nulos ou em branco mantém os valores atuais")
    void atualizacaoIgnoraNulosEBrancos() {
        Aluno aluno = Fixtures.aluno(1);

        aluno.atualizarInformacoes(new DadosAtualizacaoAluno(1L, " ", null, "", null, null));

        assertThat(aluno.getNome()).isEqualTo("Aluno 1");
        assertThat(aluno.getEmail()).isEqualTo("aluno1@email.com");
        assertThat(aluno.getTelefone()).isEqualTo("(11) 91234-5678");
        assertThat(aluno.getTipoCarta()).isEqualTo(TipoCarta.CAT_B);
    }

    @Test
    @DisplayName("Atualização nunca altera CPF nem matrícula")
    void cpfEMatriculaImutaveis() {
        Aluno aluno = Fixtures.aluno(1);

        aluno.atualizarInformacoes(new DadosAtualizacaoAluno(1L, "X", "x@email.com", "1", TipoCarta.CAT_A, null));

        assertThat(aluno.getCpf()).isEqualTo("00000000001");
        assertThat(aluno.getMatricula()).isEqualTo("00001");
    }

    @Test
    @DisplayName("Atualização repassa o endereço parcial ao Endereco")
    void atualizaEndereco() {
        Aluno aluno = Fixtures.aluno(1);
        var novoEndereco = new br.com.fiap3espg.autoescola.domain.endereco.DadosEndereco(
                null, null, null, null, "Niterói", "RJ", null);

        aluno.atualizarInformacoes(new DadosAtualizacaoAluno(1L, null, null, null, null, novoEndereco));

        assertThat(aluno.getEndereco().getCidade()).isEqualTo("Niterói");
        assertThat(aluno.getEndereco().getUf()).isEqualTo("RJ");
        assertThat(aluno.getEndereco().getLogradouro()).isEqualTo("Rua Teste");
    }

    @Test
    @DisplayName("Excluir faz exclusão lógica")
    void excluir() {
        Aluno aluno = Fixtures.aluno(1);

        aluno.excluir();

        assertThat(aluno.isAtivo()).isFalse();
    }

    @Test
    @DisplayName("Igualdade considera apenas o id")
    void igualdadePorId() {
        Aluno a = Fixtures.alunoComId(1L, 1);
        Aluno b = Fixtures.alunoComId(1L, 2);
        Aluno c = Fixtures.alunoComId(2L, 1);

        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b).isNotEqualTo(c);
    }

    @Test
    @DisplayName("DTO de detalhamento reflete a entidade")
    void detalhamento() {
        var dto = new DadosDetalhamentoAluno(Fixtures.alunoComId(7L, 3));

        assertThat(dto.id()).isEqualTo(7L);
        assertThat(dto.nome()).isEqualTo("Aluno 3");
        assertThat(dto.email()).isEqualTo("aluno3@email.com");
        assertThat(dto.cpf()).isEqualTo("00000000003");
        assertThat(dto.matricula()).isEqualTo("00003");
        assertThat(dto.endereco().uf()).isEqualTo("SP");
        assertThat(dto.ativo()).isTrue();
    }
}
