package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.Pagamentos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PagamentosRepository extends JpaRepository<Pagamentos, Long> {
    
    List<Pagamentos> findPagamentosByReservaClientId(UUID clientId);

    List<Pagamentos> findPagamentosByDataDePagamentoBefore(LocalDateTime dataDePagamentoBefore);

    List<Pagamentos> findPagamentosByDataDePagamento(LocalDateTime dataDePagamento);
}
