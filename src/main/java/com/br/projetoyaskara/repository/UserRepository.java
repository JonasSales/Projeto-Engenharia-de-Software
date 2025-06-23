package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.clientuser.ClientUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<ClientUser, UUID> {

    ClientUser findByEmail(String email);

    ClientUser findByToken(String token);

    @Query("SELECT COUNT(u) > 0 FROM ClientUser u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT c.id FROM ClientUser c WHERE c.email = :email")
    UUID findIdByEmail(@Param("email") String email);

}
