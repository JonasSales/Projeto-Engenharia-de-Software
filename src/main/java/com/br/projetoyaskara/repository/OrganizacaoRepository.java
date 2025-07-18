package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.Organizacao;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizacaoRepository extends JpaRepository<Organizacao, UUID> {

    List<Organizacao> findAllByNameContaining(@NotBlank String name);

    @Query("select o from Organizacao o where o.proprietario.id = :idProprietario")
    List<Organizacao> findAllOrganizationByIdProprietario(@Param("idProprietario") UUID idProprietario);

    Organizacao findOrganizacaoByEnderecoId(long id);

    @Query("SELECT o.id FROM Organizacao o WHERE o.id = :idEvento")
    UUID findOrganizacaoByEventosId(@Param("idEvento") UUID idEvento);
}
