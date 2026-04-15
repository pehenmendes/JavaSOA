package br.com.fiap3espg.autoescola.aluno;

import br.com.fiap3espg.autoescola.endereco.DadosEndereco;

public record AlunoDTO(
        String nome,
        String email,
        String telefone,
        String matricula,
        String tipoCarta,
        DadosEndereco endereco
) {
}
