package br.com.hyper.repositories;

import br.com.hyper.entities.ArtistEntity;

import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends UuidRepository<ArtistEntity> {
}
