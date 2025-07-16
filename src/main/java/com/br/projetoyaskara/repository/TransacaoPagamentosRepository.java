package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.TransacaoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransacaoPagamentosRepository extends JpaRepository<TransacaoPagamento, Long> {

    List<TransacaoPagamento> findTransacaoPagamentoByDataDePagamentoBefore(LocalDateTime dataDePagamentoBefore);

    List<TransacaoPagamento> findTransacaoPagamentoByDataDePagamento(LocalDateTime dataDePagamento);
}
