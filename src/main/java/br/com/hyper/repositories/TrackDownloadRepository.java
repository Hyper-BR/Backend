package br.com.hyper.repositories;

import br.com.hyper.entities.TrackDownloadEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.UUID;

@Repository
public interface TrackDownloadRepository extends LongRepository<TrackDownloadEntity> {

    @Query("SELECT COUNT(td) FROM TrackDownloadEntity td JOIN td.track.artists a WHERE a.id = :artistId")
    BigInteger countByTrackArtistId(@Param("artistId") UUID artistId);

    @Query("SELECT COUNT(td) FROM TrackDownloadEntity td JOIN td.track.artists a WHERE a.id = :artistId AND td.track.id = :trackId")
    BigInteger countByTrackArtistIdAndTrackId(@Param("artistId") UUID artistId, @Param("trackId") UUID trackId);
}
