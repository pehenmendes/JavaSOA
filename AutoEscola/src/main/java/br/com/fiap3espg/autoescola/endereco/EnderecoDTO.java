package br.com.fiap3espg.autoescola.endereco;

public record EnderecoDTO(
    String logradouro,
    String numero,
    String complemento,
    String bairro,
    String cidade,
    String uf,
    String cep) {

}
