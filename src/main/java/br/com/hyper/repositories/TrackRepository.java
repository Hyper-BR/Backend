package br.com.hyper.repositories;

import br.com.hyper.entities.ReleaseEntity;
import br.com.hyper.entities.TrackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends UuidRepository<TrackEntity> {

    @Query("SELECT o FROM TrackEntity o WHERE genre in :genres")
    Page<TrackEntity> findByGenres(@Param("genres") List<String> genres,
                                   Pageable pageable);
}
