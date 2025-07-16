package com.br.projetoyaskara.dto.response;

import com.br.projetoyaskara.model.ItemCarrinho;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrinhoResponse {

    private UUID id;

    private int quantidade;

    private LoteIngressoResponseDTO loteIngresso;

    private int subtotal;

    public ItemCarrinhoResponse(ItemCarrinho itemCarrinho) {
        this.id = itemCarrinho.getId();
        this.quantidade = itemCarrinho.getQuantidade();
        this.loteIngresso = new LoteIngressoResponseDTO(itemCarrinho.getLotesIngresso());
        this.subtotal = quantidade * loteIngresso.getValor();
    }

}