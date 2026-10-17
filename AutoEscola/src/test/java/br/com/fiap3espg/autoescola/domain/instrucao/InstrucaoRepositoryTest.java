package br.com.fiap3espg.autoescola.domain.instrucao;

import br.com.fiap3espg.autoescola.domain.aluno.Aluno;
import br.com.fiap3espg.autoescola.domain.instrutor.Especialidade;
import br.com.fiap3espg.autoescola.domain.instrutor.Instrutor;
import br.com.fiap3espg.autoescola.domain.instrutor.InstrutorRepository;
import br.com.fiap3espg.autoescola.util.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class InstrucaoRepositoryTest {
    // 07/01/2030 é uma segunda-feira
    private static final LocalDateTime SEGUNDA_10H = LocalDateTime.of(2030, 1, 7, 10, 0);

    @Autowired
    InstrucaoRepository repository;

    @Autowired
    InstrutorRepository instrutorRepository;

    @Autowired
    TestEntityManager em;

    Aluno aluno;
    Instrutor instrutor;

    @BeforeEach
    void setUp() {
        aluno = em.persist(Fixtures.aluno(21));
        instrutor = em.persist(Fixtures.instrutor(21, Especialidade.CARROS));
    }

    private Instrucao agendar(LocalDateTime dataHora) {
        return em.persistAndFlush(new Instrucao(null, aluno, instrutor, dataHora, true));
    }

    @Test
    @DisplayName("Persistir e recarregar mantém aluno, instrutor, data e status")
    void persistirERecarregar() {
        Instrucao salva = agendar(SEGUNDA_10H);
        em.clear();

        Instrucao recarregada = repository.findById(salva.getId()).orElseThrow();

        assertThat(recarregada.getDataHora()).isEqualTo(SEGUNDA_10H);
        assertThat(recarregada.isAtivo()).isTrue();
        assertThat(recarregada.getAluno().getId()).isEqualTo(aluno.getId());
        assertThat(recarregada.getInstrutor().getId()).isEqualTo(instrutor.getId());
    }

    @Test
    @DisplayName("Instrutor ocupado: verdadeiro só para a mesma data/hora")
    void instrutorOcupado() {
        agendar(SEGUNDA_10H);

        assertThat(repository.existsByInstrutorIdAndDataHoraAndAtivoTrue(instrutor.getId(), SEGUNDA_10H)).isTrue();
        assertThat(repository.existsByInstrutorIdAndDataHoraAndAtivoTrue(instrutor.getId(), SEGUNDA_10H.plusHours(1)))
                .isFalse();
        assertThat(repository.existsByInstrutorIdAndDataHoraAndAtivoTrue(-1L, SEGUNDA_10H)).isFalse();
    }

    @Test
    @DisplayName("Instrução cancelada libera o horário do instrutor")
    void canceladaLiberaInstrutor() {
        Instrucao instrucao = agendar(SEGUNDA_10H);
        instrucao.cancelar();
        em.flush();

        assertThat(repository.existsByInstrutorIdAndDataHoraAndAtivoTrue(instrutor.getId(), SEGUNDA_10H)).isFalse();
    }

    @Test
    @DisplayName("Limite diário: encontra instrução do aluno em qualquer hora do dia")
    void limiteDiarioEncontra() {
        agendar(SEGUNDA_10H);
        LocalDateTime inicio = SEGUNDA_10H.toLocalDate().atStartOfDay();
        LocalDateTime fim = SEGUNDA_10H.toLocalDate().atTime(23, 59, 59);

        assertThat(repository.existsByAlunoIdAndDataHoraBetweenAndAtivoTrue(aluno.getId(), inicio, fim)).isTrue();
    }

    @Test
    @DisplayName("Limite diário: não considera instruções de outro dia")
    void limiteDiarioOutroDia() {
        agendar(SEGUNDA_10H);
        LocalDateTime inicio = SEGUNDA_10H.plusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime fim = SEGUNDA_10H.plusDays(1).toLocalDate().atTime(23, 59, 59);

        assertThat(repository.existsByAlunoIdAndDataHoraBetweenAndAtivoTrue(aluno.getId(), inicio, fim)).isFalse();
    }

    @Test
    @DisplayName("Limite diário: instrução cancelada não conta")
    void limiteDiarioIgnoraCancelada() {
        Instrucao instrucao = agendar(SEGUNDA_10H);
        instrucao.cancelar();
        em.flush();
        LocalDateTime inicio = SEGUNDA_10H.toLocalDate().atStartOfDay();
        LocalDateTime fim = SEGUNDA_10H.toLocalDate().atTime(23, 59, 59);

        assertThat(repository.existsByAlunoIdAndDataHoraBetweenAndAtivoTrue(aluno.getId(), inicio, fim)).isFalse();
    }

    @Test
    @DisplayName("Escolha automática: instrutor com instrução cancelada volta a ficar disponível")
    void escolhaAutomaticaIgnoraCancelada() {
        Instrucao instrucao = agendar(SEGUNDA_10H);

        assertThat(instrutorRepository.escolherInstrutorAleatorioDisponivel(Especialidade.CARROS, SEGUNDA_10H))
                .isNotEqualTo(instrutor);

        instrucao.cancelar();
        em.flush();

        assertThat(instrutorRepository.escolherInstrutorAleatorioDisponivel(Especialidade.CARROS, SEGUNDA_10H))
                .isNotNull();
    }
}
