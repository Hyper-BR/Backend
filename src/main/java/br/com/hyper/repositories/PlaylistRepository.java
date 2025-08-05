package br.com.hyper.repositories;

import br.com.hyper.entities.PlaylistEntity;
import br.com.hyper.enums.Privacy;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlaylistRepository extends UuidRepository<PlaylistEntity> {

    @Query("SELECT o FROM PlaylistEntity o WHERE o.customer.id = :customerId ORDER BY o.createdDate DESC")
    List<PlaylistEntity> findByCustomerId(@Param("customerId") UUID customerId);

    @Query("SELECT o FROM PlaylistEntity o WHERE o.customer.id = :artistId AND o.privacy = :privacy ORDER BY o.createdDate DESC")
    List<PlaylistEntity> findByArtistId(@Param("artistId") UUID artistId, @Param("privacy") Privacy privacy);
}
