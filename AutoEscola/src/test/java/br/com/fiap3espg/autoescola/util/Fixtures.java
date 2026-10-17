package br.com.fiap3espg.autoescola.util;

import br.com.fiap3espg.autoescola.domain.aluno.Aluno;
import br.com.fiap3espg.autoescola.domain.aluno.TipoCarta;
import br.com.fiap3espg.autoescola.domain.endereco.DadosEndereco;
import br.com.fiap3espg.autoescola.domain.endereco.Endereco;
import br.com.fiap3espg.autoescola.domain.instrutor.Especialidade;
import br.com.fiap3espg.autoescola.domain.instrutor.Instrutor;

/**
 * Fábricas de objetos para os testes. O parâmetro "n" gera valores únicos
 * (e-mail, CPF, matrícula, CNH), respeitando as constraints unique do banco.
 */
public final class Fixtures {
    private Fixtures() {
    }

    public static DadosEndereco dadosEndereco() {
        return new DadosEndereco("Rua Teste", "100", "Apto 1", "Centro", "São Paulo", "SP", "01234-000");
    }

    public static Endereco endereco() {
        return new Endereco(dadosEndereco());
    }

    public static Aluno aluno(int n) {
        return new Aluno(
                null,
                "Aluno " + n,
                "aluno" + n + "@email.com",
                "(11) 91234-5678",
                String.format("%011d", n),
                String.format("%05d", n),
                TipoCarta.CAT_B,
                endereco(),
                true
        );
    }

    public static Aluno alunoComId(Long id, int n) {
        Aluno base = aluno(n);
        return new Aluno(id, base.getNome(), base.getEmail(), base.getTelefone(), base.getCpf(),
                base.getMatricula(), base.getTipoCarta(), base.getEndereco(), true);
    }

    public static Instrutor instrutor(int n, Especialidade especialidade) {
        return new Instrutor(
                null,
                "Instrutor " + n,
                "instrutor" + n + "@email.com",
                "(11) 98765-4321",
                String.format("%011d", n),
                especialidade,
                endereco(),
                true
        );
    }

    public static Instrutor instrutorComId(Long id, int n, Especialidade especialidade) {
        Instrutor base = instrutor(n, especialidade);
        return new Instrutor(id, base.getNome(), base.getEmail(), base.getTelefone(), base.getCnh(),
                base.getEspecialidade(), base.getEndereco(), true);
    }
}
