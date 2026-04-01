package br.com.fiap3espg.autoescola.instrutor;

import br.com.fiap3espg.autoescola.endereco.EnderecoDTO;

public record InstrutorDTO(
        String nome,
        String email,
        String cnh,
        String especialidade,
        EnderecoDTO endereco) {
}
