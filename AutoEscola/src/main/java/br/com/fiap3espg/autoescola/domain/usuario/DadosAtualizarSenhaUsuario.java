package br.com.fiap3espg.autoescola.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosAtualizarSenhaUsuario(
        @NotBlank
        String senhaAtual,

        @NotBlank
        @Size(min = 6, max = 72)
        String senhaNova
) {
}
