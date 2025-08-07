package br.com.hyper.repositories;

import br.com.hyper.entities.TrackPurchaseEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface TrackPurchaseRepository extends LongRepository<TrackPurchaseEntity> {

    @Query("SELECT SUM(tp.price) FROM TrackPurchaseEntity tp WHERE tp.track.id = :trackId")
    BigDecimal sumPriceByTrackId(@Param("trackId") UUID trackId);

    @Query("SELECT COUNT(DISTINCT tp.id) FROM TrackPurchaseEntity tp WHERE tp.track.id = :trackId")
    Long countDistinctByTrackId(@Param("trackId") UUID trackId);
}


