package br.com.hyper.repositories;

import br.com.hyper.entities.ReleaseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends UuidRepository<ReleaseEntity> {

}
