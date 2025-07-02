package br.com.hyper.repositories;

import br.com.hyper.entities.ReleaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReleaseRepository extends BaseRepository<ReleaseEntity> {

    @Query("SELECT o FROM ReleaseEntity o WHERE genre in :genres")
    Page<ReleaseEntity> findByGenres(@Param("genres") List<String> genres,
                                     Pageable pageable);
}
