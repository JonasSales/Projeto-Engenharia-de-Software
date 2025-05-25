package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.AvaliacoesEventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvaliacoesEventosRepository extends JpaRepository<AvaliacoesEventos, Integer> {

    List<AvaliacoesEventos> findByEventoId(long evento_id);

    List<AvaliacoesEventos> findByClientUserId(UUID clientUser_id);

}
