package br.com.fiap3espg.autoescola.adapter.out.repository.persistence;

import br.com.fiap3espg.autoescola.adapter.out.repository.entity.InstrutorEntity;
import br.com.fiap3espg.autoescola.application.core.domain.Instrutor;
import br.com.fiap3espg.autoescola.common.vo.enumeration.Especialidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface InstrutorJPARepository extends JpaRepository<InstrutorEntity, Long> {
    Page<InstrutorEntity> findAllByAtivoTrue(Pageable paginacao);

    @Query("""
        select i from Instrutor i
        where
        i.ativo = true
        and
        i.especialidade = :especialidade
        and
        i.id not in (
            select a.instrutor.id from Instrucao a
            where
            a.dataHora = :dataHora
            )
        order by rand()
        limit 1
    """)
    InstrutorEntity escolherInstrutorAleatorioDisponivel(Especialidade especialidade, LocalDateTime dataHora);

    boolean existsByIdAndAtivoFalse(Long id);
}
