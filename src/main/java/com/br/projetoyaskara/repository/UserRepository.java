package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.ClientUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<ClientUser, UUID> {

    ClientUser findByEmail(String email);

    ClientUser findByToken(String token);

    ClientUser findClientUserById(UUID id);

}
