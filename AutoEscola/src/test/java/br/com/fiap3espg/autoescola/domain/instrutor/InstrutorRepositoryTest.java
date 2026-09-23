package br.com.fiap3espg.autoescola.domain.instrutor;

import br.com.fiap3espg.autoescola.domain.aluno.Aluno;
import br.com.fiap3espg.autoescola.domain.aluno.TipoCarta;
import br.com.fiap3espg.autoescola.domain.endereco.Endereco;
import br.com.fiap3espg.autoescola.domain.instrucao.Instrucao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class InstrutorRepositoryTest {
    @Autowired
    InstrutorRepository repository;

    @Autowired
    TestEntityManager testEntity;

    @Test
    @DisplayName("Expectativa: retornar null quando instrutor cadastrado não estiver disponível")
    void escolherInstrutorAleatorioDisponivelCenario1(){
        LocalDateTime proximaSegunda10h = LocalDateTime
                .now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .withHour(10).withMinute(0).withSecond(0).withNano(0);

        // cadastrar aluno
        Aluno aluno = cadastrarAluno();

        // cadastrar instrutor
        Instrutor instrutor = cadastrarInstrutor();

        // agendar instrução
        agendarInstrucao(aluno, instrutor, proximaSegunda10h);

        // when or act
        Instrutor instrutorDisponivel = repository.escolherInstrutorAleatorioDisponivel(
                Especialidade.MOTOS,
                proximaSegunda10h
        );

        //then or assert
        assertThat(instrutorDisponivel).isNull();
    }

    @Test
    @DisplayName("Expectativa: retornar instrutor quando instrutor cadastrado estiver disponível")
    void escolherInstrutorAleatorioDisponivelCenario2(){
        LocalDateTime proximaSegunda10h = LocalDateTime
                .now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .withHour(10).withMinute(0).withSecond(0).withNano(0);

        // cadastrar instrutor
        Instrutor instrutor = cadastrarInstrutor();

        // when or act
        Instrutor instrutorDisponivel = repository.escolherInstrutorAleatorioDisponivel(
                Especialidade.MOTOS,
                proximaSegunda10h
        );

        //then or assert
        assertThat(instrutorDisponivel).isEqualTo(instrutor);
    }

    private Aluno cadastrarAluno() {
        Aluno aluno = new Aluno(
                null,
                "Aluno Teste",
                "alunoteste@email.com",
                "(11) 91234-5678",
                "12345678901",
                "123456",
                TipoCarta.CAT_A,
                dadosEndereco(),
                true
        );
        testEntity.persist(aluno);
        return aluno;
    }

    private Instrutor cadastrarInstrutor() {
        Instrutor instrutor = new Instrutor(
                null,
                "Instrutor Teste",
                "instrutorteste@email.com",
                "(11) 91234-5678",
                "12345678901",
                Especialidade.MOTOS,
                dadosEndereco(),
                true
        );
        testEntity.persist(instrutor);
        return instrutor;
    }

    private void agendarInstrucao(
            Aluno aluno,
            Instrutor instrutor,
            LocalDateTime dataHora) {
        Instrucao instrucao = new Instrucao(
                null,
                aluno,
                instrutor,
                dataHora,
                true
        );
        testEntity.persist(instrucao);
    }

    private Endereco dadosEndereco() {
        return new Endereco(
                "Rua teste",
                "000",
                "Casa dos fundos",
                "Vila teste",
                "Test City",
                "TS",
                "01234-000"
        );
    }
    
}
