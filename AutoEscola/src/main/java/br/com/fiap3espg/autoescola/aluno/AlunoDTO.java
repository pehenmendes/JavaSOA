package br.com.fiap3espg.autoescola.aluno;

import br.com.fiap3espg.autoescola.endereco.EnderecoDTO;

public record AlunoDTO(
        String nome,
        String email,
        String telefone,
        String matricula,
        String tipoCarta,
        EnderecoDTO endereco
) {
}
