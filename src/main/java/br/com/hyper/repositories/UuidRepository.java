package br.com.hyper.repositories;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface UuidRepository<T> extends BaseRepository<T, UUID> {
    Optional<T> findById(UUID id);
}


