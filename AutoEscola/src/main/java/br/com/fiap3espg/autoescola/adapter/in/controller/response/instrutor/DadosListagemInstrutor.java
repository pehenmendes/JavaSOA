package br.com.fiap3espg.autoescola.adapter.in.controller.response.instrutor;

import br.com.fiap3espg.autoescola.application.core.domain.Instrutor;
import br.com.fiap3espg.autoescola.common.vo.enumeration.Especialidade;

public record DadosListagemInstrutor(
        Long id,
        String nome,
        String email,
        Especialidade especialidade) {
    public DadosListagemInstrutor(Instrutor instrutor) {
        this(
                instrutor.getId(),
                instrutor.getNome(),
                instrutor.getEmail(),
                instrutor.getEspecialidade());
    }
}
