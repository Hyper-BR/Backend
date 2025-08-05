package br.com.hyper.repositories;

import br.com.hyper.entities.ReleaseEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReleaseRepository extends UuidRepository<ReleaseEntity> {

    @Query("SELECT r FROM ReleaseEntity r LEFT JOIN FETCH r.tracks t WHERE r.owner = :artistId")
    List<ReleaseEntity> findByArtistId(UUID artistId);
}
