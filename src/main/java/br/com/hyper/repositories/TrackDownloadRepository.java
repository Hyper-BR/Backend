package br.com.hyper.repositories;

import br.com.hyper.entities.TrackDownloadEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrackDownloadRepository extends LongRepository<TrackDownloadEntity> {

    @Query("SELECT COUNT(DISTINCT td.id) FROM TrackDownloadEntity td WHERE td.track.id = :trackId")
    Long countDistinctByTrackId(@Param("trackId") UUID trackId);
}
