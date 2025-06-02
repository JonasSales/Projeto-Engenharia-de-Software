package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.AvaliacoesEventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvaliacoesEventosRepository extends JpaRepository<AvaliacoesEventos, Long> {

    AvaliacoesEventos findAvaliacoesEventosById(Long id);

    List<AvaliacoesEventos> findAvaliacoesEventosByEventoId(long evento);

    List<AvaliacoesEventos> findAvaliacoesEventosByClientUserId(UUID clientUser_id);

    long countAvaliacoesEventosByEventoId(long eventoId);

    @Query("SELECT AVG(a.nota) FROM AvaliacoesEventos a WHERE a.evento.id = :eventoId")
    Double notaMediaEvento(@Param("eventoId") long eventoId);

}
