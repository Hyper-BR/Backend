package br.com.hyper.repositories;

import br.com.hyper.entities.ArtistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends UuidRepository<ArtistEntity> {

    @Query("SELECT o FROM ArtistEntity o WHERE name in :names")
    Page<ArtistEntity> findByUsernamePage(@Param("names") List<String> names,
                                          Pageable pageable);

    @Query("SELECT o FROM ArtistEntity o WHERE name = :name")
    Optional<ArtistEntity> findByUsername(@Param("name") String name);

}
