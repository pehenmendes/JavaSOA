package br.com.fiap3espg.autoescola.adapter.in.controller.request.aluno;

import br.com.fiap3espg.autoescola.common.vo.endereco.DadosEndereco;
import br.com.fiap3espg.autoescola.common.vo.enumeration.TipoCarta;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoAluno(
        @NotNull
        Long id,
        String nome,
        String email,
        String telefone,
        TipoCarta tipoCarta,
        DadosEndereco endereco
) {
}
