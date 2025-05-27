package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.LotesIngresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LotesIngressosRepository extends JpaRepository<LotesIngresso, Long> {

    LotesIngresso findLotesIngressoById(Long id);

    LotesIngresso findLotesIngressoByEventoId(Long eventoId);

    @Query("select e from LotesIngresso e where ((e.valor > :primeiraFaixa) and (e.valor < :segundaFaixa)) ")
    List<LotesIngresso> findLotesIngressosPorFaixaDePreco(@Param("primeiraFaixa") int primeiraFaixa,
                                                          @Param("segundaFaixa") int segundaFaixa);
}
