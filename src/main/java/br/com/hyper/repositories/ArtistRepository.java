package br.com.hyper.repositories;

import br.com.hyper.entities.ArtistEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtistRepository extends UuidRepository<ArtistEntity> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM ArtistEntity a WHERE a.id = :customerId")
    boolean existsByCustomerId(@Param("customerId") UUID customerId);

    @Query("SELECT a FROM ArtistEntity a WHERE lower(a.username) like lower(concat('%', :q, '%'))")
    Page<ArtistEntity> searchByName(@Param("q") String q, Pageable pageable);
}
