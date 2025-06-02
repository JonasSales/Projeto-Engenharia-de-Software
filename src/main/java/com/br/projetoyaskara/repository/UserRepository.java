package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.ClientUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<ClientUser, UUID> {

    ClientUser findByEmail(String email);

    ClientUser findByToken(String token);

    ClientUser findClientUserById(UUID id);

    @Query("SELECT COUNT(u) > 0 FROM ClientUser u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM ClientUser c WHERE c.email = :email")
    void deleteClientUserByEmail(@Param("email") String email);
}
