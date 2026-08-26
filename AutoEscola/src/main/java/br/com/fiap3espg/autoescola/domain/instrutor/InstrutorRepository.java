package br.com.fiap3espg.autoescola.domain.instrutor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {
    Page<Instrutor> findAllByAtivoTrue(Pageable paginacao);

    Instrutor escolherInstrutorAleatorioDisponivel(Especialidade especialidade, LocalDateTime localDateTime);
}
