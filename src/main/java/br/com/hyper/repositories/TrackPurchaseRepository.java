package br.com.hyper.repositories;

import br.com.hyper.entities.TrackPurchaseEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Repository
public interface TrackPurchaseRepository extends LongRepository<TrackPurchaseEntity> {

    @Query("SELECT COUNT(tp) FROM TrackPurchaseEntity tp JOIN tp.track.artists a WHERE a.id = :artistId")
    BigInteger countByArtistId(@Param("artistId") UUID artistId);

    @Query("SELECT COUNT(tp) FROM TrackPurchaseEntity tp JOIN tp.track.artists a WHERE a.id = :artistId AND tp.track.id = :trackId")
    BigInteger countByArtistIdAndTrackId(@Param("artistId") UUID artistId, @Param("trackId") UUID trackId);

    @Query("SELECT SUM(tp.amount) FROM TrackPurchaseEntity tp JOIN tp.track.artists a WHERE a.id = :artistId")
    BigDecimal sumRevenueByArtistId(@Param("artistId") UUID artistId);

    @Query("SELECT SUM(tp.amount) FROM TrackPurchaseEntity tp JOIN tp.track.artists a WHERE a.id = :artistId AND tp.track.id = :trackId")
    BigDecimal sumRevenueByArtistIdAndTrackId(@Param("artistId") UUID artistId, @Param("trackId") UUID trackId);
}


