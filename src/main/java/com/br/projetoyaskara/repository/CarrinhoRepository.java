package com.br.projetoyaskara.repository;

import com.br.projetoyaskara.model.Carrinho;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, Integer> {

    Carrinho findByClientUserId(UUID idUsuario);
}
