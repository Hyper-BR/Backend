package br.com.hyper.repositories;

import br.com.hyper.entities.PlayHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.UUID;

@Repository
public interface PlayHistoryRepository extends LongRepository<PlayHistoryEntity> {

    @Query("SELECT COUNT(ph) FROM PlayHistoryEntity ph JOIN ph.track.artists a WHERE a.id = :artistId")
    BigInteger countByTrackArtistId(@Param("artistId") UUID artistId);

    @Query("SELECT COUNT(ph) FROM PlayHistoryEntity ph JOIN ph.track.artists a WHERE a.id = :artistId AND ph.track.id = :trackId")
    BigInteger countByTrackArtistIdAndTrackId(@Param("artistId") UUID artistId, @Param("trackId") UUID trackId);
}