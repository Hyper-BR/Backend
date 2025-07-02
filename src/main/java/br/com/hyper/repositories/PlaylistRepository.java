package br.com.hyper.repositories;

import br.com.hyper.entities.PlaylistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends BaseRepository<PlaylistEntity> {

    @Query("SELECT o FROM PlaylistEntity o WHERE o.name = :name")
    Page<PlaylistEntity> findByName(@Param("name") String name,
                                    Pageable pageable);

    @Query("SELECT o FROM PlaylistEntity o WHERE o.customer.id = :customerId")
    List<PlaylistEntity> findByCustomerId(@Param("customerId") Long customerId);

}
