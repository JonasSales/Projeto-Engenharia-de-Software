package com.br.projetoyaskara.dto.response;

import com.br.projetoyaskara.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDTO {

    private UUID id;
    private UUID clientUserId;
    private List<ItemPedidoResponseDTO> itens;
    private TransacaoPagamentoResponseDTO transacao;
    private Pedido.StatusPedido status;
    private Integer valorTotal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public PedidoResponseDTO(Pedido pedidoSalvo) {
        this.id = pedidoSalvo.getId();
        this.clientUserId = pedidoSalvo.getClientUser().getId();
        this.itens = pedidoSalvo.getItensPedido().stream().map(ItemPedidoResponseDTO::new).toList();
        this.transacao = new TransacaoPagamentoResponseDTO(pedidoSalvo.getTransacaoPagamento());
        this.status = pedidoSalvo.getStatus();
        this.valorTotal = pedidoSalvo.getValorTotal();
        this.dataCriacao = pedidoSalvo.getDataCriacao();
        this.dataAtualizacao = pedidoSalvo.getDataAtualizacao();
    }
}