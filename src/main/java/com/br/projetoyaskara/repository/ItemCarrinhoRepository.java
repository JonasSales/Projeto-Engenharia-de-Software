package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.ItemCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, UUID> {

    List<ItemCarrinho> findAllByCarrinhoId(UUID carrinhoId);
}
