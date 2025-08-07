package br.com.hyper.repositories;

import br.com.hyper.dtos.ListenerDTO;
import br.com.hyper.entities.PlayHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlayHistoryRepository extends LongRepository<PlayHistoryEntity> {

    @Query("SELECT COUNT(DISTINCT ph.id) FROM PlayHistoryEntity ph WHERE ph.track.id = :trackId")
    Long countDistinctByTrackId(@Param("trackId") UUID trackId);

    @Query("""
    SELECT c.id AS id, c.name AS name, COUNT(ph.id) AS plays
    FROM PlayHistoryEntity ph
    JOIN ph.customer c
    JOIN ph.track t
    JOIN t.artists a
    WHERE a.id = :artistId
    GROUP BY c.id, c.name
    ORDER BY COUNT(ph.id) DESC
""")
    List<ListenerDTO> findTopListenersWithNameByArtistId(@Param("artistId") UUID artistId);

}