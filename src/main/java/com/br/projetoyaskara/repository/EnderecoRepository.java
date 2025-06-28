package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.Endereco;
import com.nimbusds.openid.connect.sdk.assurance.evidences.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
