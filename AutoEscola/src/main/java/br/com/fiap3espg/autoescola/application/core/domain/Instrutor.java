package br.com.fiap3espg.autoescola.application.core.domain;


import br.com.fiap3espg.autoescola.common.vo.endereco.Endereco;
import br.com.fiap3espg.autoescola.adapter.in.controller.request.instrutor.DadosAtualizacaoInstrutor;
import br.com.fiap3espg.autoescola.adapter.in.controller.request.instrutor.DadosCadastroInstrutor;
import br.com.fiap3espg.autoescola.common.vo.enumeration.Especialidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Instrutor {
    private Long id;

    private String nome;
    private String email;
    private String telefone;
    private String cnh;
    private Especialidade especialidade;
    private Endereco endereco;
    private boolean ativo = true;

    public Instrutor() {
    }

    public Instrutor(Long id,
                     String nome,
                     String email,
                     String telefone,
                     String cnh,
                     Especialidade especialidade,
                     Endereco endereco,
                     boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cnh = cnh;
        this.especialidade = especialidade;
        this.endereco = endereco;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCnh() {
        return cnh;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Instrutor(DadosCadastroInstrutor dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cnh = dados.cnh();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
    }

    public void atualizarInformacoes(DadosAtualizacaoInstrutor dados) {
        if (dados.nome() != null && !dados.nome().isBlank()){
            this.nome = dados.nome();
        }
        if (dados.email() != null && !dados.email().isBlank()){
            this.email = dados.email();
        }
        if (dados.telefone() != null && !dados.telefone().isBlank()){
            this.telefone = dados.telefone();
        }
        if (dados.especialidade() != null){
            this.especialidade = dados.especialidade();
        }
        if (dados.endereco() != null){
            this.endereco.atualizarInformacoes(dados.endereco());
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}
