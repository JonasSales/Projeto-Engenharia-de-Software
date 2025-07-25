package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.ItemPedido;
import com.br.projetoyaskara.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
}
