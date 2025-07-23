package com.br.projetoyaskara.dto.response;

import com.br.projetoyaskara.model.Carrinho;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoResponse {

    private UUID id;

    private UUID clientUserId;

    private List<ItemCarrinhoResponse> itens;

    private double valorTotal;

    public CarrinhoResponse(Carrinho carrinho) {
        this.id = carrinho.getId();
        this.clientUserId = carrinho.getClientUser().getId();
        this.itens = carrinho.getItensCarrinho().stream()
                .map(ItemCarrinhoResponse::new)
                .toList();


        this.valorTotal = carrinho.getItensCarrinho().stream()
                .mapToDouble(item -> item.getQuantidade() * item.getLotesIngresso().getValor() / 100.0)
                .sum();
    }


}