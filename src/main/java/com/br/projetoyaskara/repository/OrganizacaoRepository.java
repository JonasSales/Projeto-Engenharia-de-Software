package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.Organizacao;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizacaoRepository extends JpaRepository<Organizacao, UUID> {

    List<Organizacao> findAllByNameContaining(@NotBlank String name);

}
