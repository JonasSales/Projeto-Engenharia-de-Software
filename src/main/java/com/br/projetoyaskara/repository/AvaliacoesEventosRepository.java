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

    List<AvaliacoesEventos> findAvaliacoesEventosByEventoId(long evento);

    @Query("select a from AvaliacoesEventos a where a.clientUser.email = :email")
    List<AvaliacoesEventos> findAvaliacoesEventosByClientUserEmail(@Param("email") String email);

    @Query("select a from AvaliacoesEventos a where (a.clientUser.id = :idClient and a.evento.id =: eventoId)")
    List<AvaliacoesEventos> avaliacoesDoClientPorEvento(@Param("email") UUID idClient, @Param("eventoId") Long eventoId);

    @Query("SELECT AVG(a.nota) FROM AvaliacoesEventos a WHERE a.evento.id = :eventoId")
    Double notaMediaEvento(@Param("eventoId") long eventoId);

}
