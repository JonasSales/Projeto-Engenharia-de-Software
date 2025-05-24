package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.ClientUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<ClientUser, Long> {



    ClientUser findByEmail(String email);

    ClientUser findByToken(String token);
}
