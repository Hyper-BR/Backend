package br.com.hyper.repositories;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface LongRepository<T> extends BaseRepository<T, Long> {
    Optional<T> findById(Long id);
}

