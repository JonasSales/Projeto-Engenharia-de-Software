package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.LotesIngresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotesIngressosRepository extends JpaRepository<LotesIngresso, Long> {
}
