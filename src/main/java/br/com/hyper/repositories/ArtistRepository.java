package br.com.hyper.repositories;

import br.com.hyper.entities.ArtistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends BaseRepository<ArtistEntity> {

    @Query("SELECT o FROM ArtistEntity o WHERE username in :usernames")
    Page<ArtistEntity> findByUsernamePage(@Param("usernames") List<String> usernames,
                                          Pageable pageable);

    @Query("SELECT o FROM ArtistEntity o WHERE username = :username")
    Optional<ArtistEntity> findByUsername(@Param("username") String username);

}
