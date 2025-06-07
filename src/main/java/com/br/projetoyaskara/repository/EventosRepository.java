package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.Eventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EventosRepository extends JpaRepository<Eventos, Long> {


    @Query("select e from Eventos e where e.organizacao.name like concat('%', :organizacaoName, '%')")
    List<Eventos> findAllByOrganizacao_Name(@Param("organizacaoName") String organizacaoName);

    List<Eventos> findAllByDescricaoContaining(String descricao);

    List<Eventos> findAllByOrganizacao_Id(UUID organizacaoId);

    List<Eventos> findAllByFaixaEtaria(Eventos.FaixaEtaria faixaEtaria);

    List<Eventos> findAllByStatus(Eventos.Status status);
}
