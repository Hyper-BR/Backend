package br.com.hyper.repositories;

import br.com.hyper.entities.SubscriptionEntity;
import br.com.hyper.entities.TrackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends BaseRepository<SubscriptionEntity> {

}
