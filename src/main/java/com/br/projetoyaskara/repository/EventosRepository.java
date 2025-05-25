package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.model.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventosRepository extends JpaRepository<Eventos, Long> {

    Eventos findById(long id);

    List<Eventos> findAllByDescricaoContaining(String descricao);

    List<Eventos> findAllByOrganizacao(Organizacao organizacao);

    List<Eventos> findAllByFaixaEtaria(Eventos.FaixaEtaria faixaEtaria);
}
