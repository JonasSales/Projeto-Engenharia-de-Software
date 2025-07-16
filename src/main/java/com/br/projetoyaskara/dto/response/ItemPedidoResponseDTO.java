package com.br.projetoyaskara.dto.response;

import com.br.projetoyaskara.model.ItemPedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedidoResponseDTO{
    private UUID id;
    private UUID pedidoId;
    private UUID loteIngressoId;
    private String nomeIngresso;
    private int valo;

    public ItemPedidoResponseDTO(ItemPedido itemPedido){
        this.id = itemPedido.getId();
        this.pedidoId = itemPedido.getPedido().getId();
        this.loteIngressoId = itemPedido.getLotesIngresso().getId();
        this.nomeIngresso = itemPedido.getNomeIngresso();
        this.valo = itemPedido.getValor();
    }
}

