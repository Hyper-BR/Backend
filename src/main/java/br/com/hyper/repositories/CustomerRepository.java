package br.com.hyper.repositories;

import br.com.hyper.entities.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends UuidRepository<CustomerEntity> {

    @Query("SELECT o FROM CustomerEntity o WHERE email = :email")
    UserDetails findByEmailUserDetails(@Param("email") String email);

    @Query("SELECT o FROM CustomerEntity o WHERE id = :id")
    UserDetails findByIdUserDetails(@Param("id") UUID id);

    @Query("SELECT o FROM CustomerEntity o WHERE email = :email")
    Optional<CustomerEntity> findByEmail(@Param("email")String email);

    @Query("SELECT o FROM CustomerEntity o WHERE name in: names")
    Page<CustomerEntity> findByName(@Param("names")List<String> names,
                                    Pageable pageable);

}
