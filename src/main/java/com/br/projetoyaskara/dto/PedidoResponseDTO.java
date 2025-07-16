package com.br.projetoyaskara.dto;

import com.br.projetoyaskara.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
    private BigDecimal valorTotal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

}