package br.com.fiap3espg.autoescola.domain.aluno;

import br.com.fiap3espg.autoescola.util.Fixtures;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class AlunoRepositoryTest {
    @Autowired
    AlunoRepository repository;

    @Autowired
    TestEntityManager em;

    @Test
    @DisplayName("Persistir e recarregar mantém os dados e o endereço embutido")
    void persistirERecarregar() {
        Aluno aluno = em.persistAndFlush(Fixtures.aluno(1));
        em.clear();

        Aluno recarregado = repository.findById(aluno.getId()).orElseThrow();

        assertThat(recarregado.getNome()).isEqualTo("Aluno 1");
        assertThat(recarregado.getTipoCarta()).isEqualTo(TipoCarta.CAT_B);
        assertThat(recarregado.getEndereco().getCidade()).isEqualTo("São Paulo");
        assertThat(recarregado.getEndereco().getCep()).isEqualTo("01234-000");
        assertThat(recarregado.isAtivo()).isTrue();
    }

    @Test
    @DisplayName("findAllByAtivoTrue não retorna alunos excluídos logicamente")
    void listarSomenteAtivos() {
        Aluno ativo = em.persist(Fixtures.aluno(2));
        Aluno inativo = Fixtures.aluno(3);
        inativo.excluir();
        em.persist(inativo);
        em.flush();

        var pagina = repository.findAllByAtivoTrue(PageRequest.of(0, 100));

        assertThat(pagina.getContent()).contains(ativo).doesNotContain(inativo);
    }

    @Test
    @DisplayName("existsByIdAndAtivoFalse só é verdadeiro para aluno inativo")
    void existsAtivoFalse() {
        Aluno ativo = em.persist(Fixtures.aluno(4));
        Aluno inativo = Fixtures.aluno(5);
        inativo.excluir();
        em.persist(inativo);
        em.flush();

        assertThat(repository.existsByIdAndAtivoFalse(ativo.getId())).isFalse();
        assertThat(repository.existsByIdAndAtivoFalse(inativo.getId())).isTrue();
        assertThat(repository.existsByIdAndAtivoFalse(-1L)).isFalse();
    }

    @Test
    @DisplayName("CPF duplicado viola a constraint unique")
    void cpfDuplicado() {
        em.persistAndFlush(Fixtures.aluno(6));
        Aluno duplicado = Fixtures.aluno(7);
        // mesmo CPF do aluno 6
        Aluno comCpfRepetido = new Aluno(null, duplicado.getNome(), duplicado.getEmail(), duplicado.getTelefone(),
                Fixtures.aluno(6).getCpf(), duplicado.getMatricula(), duplicado.getTipoCarta(),
                duplicado.getEndereco(), true);

        assertThatThrownBy(() -> em.persistAndFlush(comCpfRepetido)).isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("E-mail duplicado viola a constraint unique")
    void emailDuplicado() {
        em.persistAndFlush(Fixtures.aluno(8));
        Aluno base = Fixtures.aluno(9);
        Aluno comEmailRepetido = new Aluno(null, base.getNome(), Fixtures.aluno(8).getEmail(), base.getTelefone(),
                base.getCpf(), base.getMatricula(), base.getTipoCarta(), base.getEndereco(), true);

        assertThatThrownBy(() -> em.persistAndFlush(comEmailRepetido)).isInstanceOf(PersistenceException.class);
    }

    @Test
    @DisplayName("Matrícula duplicada viola a constraint unique")
    void matriculaDuplicada() {
        em.persistAndFlush(Fixtures.aluno(10));
        Aluno base = Fixtures.aluno(11);
        Aluno comMatriculaRepetida = new Aluno(null, base.getNome(), base.getEmail(), base.getTelefone(),
                base.getCpf(), Fixtures.aluno(10).getMatricula(), base.getTipoCarta(), base.getEndereco(), true);

        assertThatThrownBy(() -> em.persistAndFlush(comMatriculaRepetida)).isInstanceOf(PersistenceException.class);
    }
}
