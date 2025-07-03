package br.com.hyper.repositories;

import br.com.hyper.entities.ArtistEntity;

import br.com.hyper.entities.CustomerEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends UuidRepository<ArtistEntity> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM ArtistEntity a WHERE a.customer = ?1")
    boolean existsByCustomer(CustomerEntity customer);
}
