package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.AvaliacoesEventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvaliacoesEventosRepository extends JpaRepository<AvaliacoesEventos, Integer> {

    List<AvaliacoesEventos> findByEventoId(long evento_id);

    List<AvaliacoesEventos> findByClientUserId(UUID clientUser_id);

    List<AvaliacoesEventos> findByNotaGreaterThanEqual(int notaMinima);

    List<AvaliacoesEventos> findByEventoIdOrderByHoraAvaliacaoDesc(long eventoId);

    long countByEventoId(long eventoId);

    @Query("SELECT AVG(a.nota) FROM AvaliacoesEventos a WHERE a.evento.id = :eventoId")
    Double findAverageNotaByEventoId(@Param("eventoId") long eventoId);

}
