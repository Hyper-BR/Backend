package br.com.hyper.repositories;

import br.com.hyper.entities.CartEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends UuidRepository<CartEntity> {

    @Query("SELECT c FROM CartEntity c WHERE c.customer.id = :customerId")
    List<CartEntity> findByCustomerId(@Param("customerId") UUID customerId);

}
