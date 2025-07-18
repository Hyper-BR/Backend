package br.com.hyper.repositories;

import br.com.hyper.entities.TrackEntity;
import br.com.hyper.enums.Privacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrackRepository extends UuidRepository<TrackEntity> {

    @Query("SELECT t FROM TrackEntity t JOIN t.artists a WHERE a.id = :artistId")
    Page<TrackEntity> findByArtistId(@Param("artistId") UUID artistId, Pageable pageable);

    @Query("select DISTINCT t" +
           " FROM TrackEntity t" +
           " LEFT JOIN t.artists a" +
           " WHERE lower(t.title) like lower(concat('%', :q, '%'))" +
           " or lower(a.username) like lower(concat('%', :q, '%'))" +
           " AND t.privacy = :privacy")
    Page<TrackEntity> searchByTitleOrArtist(@Param("q") String q,
                                            @Param("privacy") Privacy privacy,
                                            Pageable pageable);

    @Query("SELECT t FROM TrackEntity t JOIN t.genres g WHERE g.name = :name")
    List<TrackEntity> findByGenreName(@Param("name") String name);

}
