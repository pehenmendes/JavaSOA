package br.com.fiap3espg.autoescola.adapter.out.repository;

import br.com.fiap3espg.autoescola.adapter.out.repository.entity.InstrutorEntity;
import br.com.fiap3espg.autoescola.adapter.out.repository.mapper.InstrutorEntityMapper;
import br.com.fiap3espg.autoescola.adapter.out.repository.persistence.InstrutorJPARepository;
import br.com.fiap3espg.autoescola.application.core.domain.Instrutor;
import br.com.fiap3espg.autoescola.application.port.out.InstrutorRepository;
import br.com.fiap3espg.autoescola.common.vo.enumeration.Especialidade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InstrutorRepositoryImpl implements InstrutorRepository {
    private final InstrutorJPARepository jpaRepository;
    private final InstrutorEntityMapper entityMapper;

    @Override
    public Page<Instrutor> findAllByAtivoTrue(Pageable paginacao) {
        return jpaRepository.findAllByAtivoTrue(paginacao).map(entityMapper::toDomain);
    }

    @Override
    public Instrutor escolherInstrutorAleatorioDisponivel(Especialidade especialidade, LocalDateTime dataHora) {
        InstrutorEntity entity = jpaRepository.escolherInstrutorAleatorioDisponivel(especialidade, dataHora);
        return entityMapper.toDomain(entity);
    }

    @Override
    public boolean existsByIdAndAtivoFalse(Long id) {
        return jpaRepository.existsByIdAndAtivoFalse(id);
    }

    @Override
    public Instrutor save(Instrutor instrutor) {
        InstrutorEntity entity = entityMapper.toEntity(instrutor);
        InstrutorEntity saved = jpaRepository.save(entity);
        return entityMapper.toDomain(saved);
    }
}
