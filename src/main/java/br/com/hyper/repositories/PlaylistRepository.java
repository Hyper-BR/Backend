package br.com.hyper.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends BaseRepository<PlaylistEntity> {

    @Query("SELECT o FROM PlaylistEntity o WHERE o.name = :name")
    Page<PlaylistEntity> findByName(@Param("name") String name,
                                  Pageable pageable);

    @Query("SELECT o FROM PlaylistEntity o WHERE o.id = :id")
    List<PlaylistEntity> findByCustomerId(@Param("id") Long customerId);

    @Query("SELECT p FROM PlaylistEntity p JOIN FETCH p.tracks WHERE p.id = :id")
    Optional<PlaylistEntity> findWithTracks(Long id);
}
