package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.model.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventosRepository extends JpaRepository<Eventos, Long> {

    Eventos findById(long id);

    List<Eventos> findByDescricaoContainingIgnoreCase(String descricao);

    List<Eventos> findByOrganizacao(Organizacao organizacao);

    List<Eventos> findEventosByFaixaEtaria(Eventos.FaixaEtaria faixaEtaria);
}
