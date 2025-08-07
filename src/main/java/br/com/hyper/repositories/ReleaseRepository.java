package br.com.hyper.repositories;

import br.com.hyper.entities.ReleaseEntity;
import br.com.hyper.enums.Privacy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReleaseRepository extends UuidRepository<ReleaseEntity> {

    @Query("SELECT r FROM ReleaseEntity r LEFT JOIN FETCH r.tracks t WHERE r.owner = :artistId ORDER BY r.createdDate DESC")
    List<ReleaseEntity> findByArtistId(UUID artistId);

    @Query("SELECT DISTINCT r FROM ReleaseEntity r JOIN r.tracks t WHERE r.owner = :artistId AND t.privacy = :privacy ORDER BY r.createdDate DESC")
    Page<ReleaseEntity> findReleasesByArtistIdAndPrivacy(@Param("artistId") UUID artistId,
                                                         @Param("privacy") Privacy privacy,
                                                         Pageable pageable);

    @Query("SELECT DISTINCT r FROM ReleaseEntity r JOIN r.tracks t WHERE r.owner = :customerId ORDER BY r.createdDate DESC")
    Page<ReleaseEntity> findReleasesByCustomer(@Param("customerId") UUID customerId, Pageable pageable);

}
