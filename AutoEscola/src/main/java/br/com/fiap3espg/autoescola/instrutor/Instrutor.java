package br.com.fiap3espg.autoescola.instrutor;


import br.com.fiap3espg.autoescola.endereco.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Instrutor")
@Table(name = "instrutores")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Instrutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String cnh;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Embedded
    private Endereco endereco;

    public Instrutor(DadosCadastroInstrutor dados) {
        this.nome = dados.nome();
        this.email = dados.email();
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
        if (dados.especialidade() != null){
            this.especialidade = dados.especialidade();
        }
        if (dados.endereco() != null){
            this.endereco.atualizarInformacoes(dados.endereco());
        }
    }
}
