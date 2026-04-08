package br.com.fiap3espg.autoescola.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoDTO(
    @NotBlank
    String logradouro,

    String numero,
    String complemento,

    @NotBlank
    String bairro,

    @NotBlank
    String cidade,

    @NotBlank
    @Pattern(regexp = "[A-Z]{2}")
    String uf,

    @NotBlank
    @Pattern(regexp = "[0-9]{5}-[0-9]{3}")
    String cep) {

}
