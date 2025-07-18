package br.com.hyper.repositories;

import br.com.hyper.entities.LabelEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository extends UuidRepository<LabelEntity> {
}
