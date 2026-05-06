package br.com.fiap3espg.autoescola.domain.aluno;

import br.com.fiap3espg.autoescola.domain.endereco.DadosEndereco;

public record AlunoDTO(
        String nome,
        String email,
        String telefone,
        String matricula,
        String tipoCarta,
        DadosEndereco endereco
) {
}
