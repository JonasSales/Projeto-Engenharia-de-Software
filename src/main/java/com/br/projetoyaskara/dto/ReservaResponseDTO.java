package com.br.projetoyaskara.dto;

import com.br.projetoyaskara.model.Reservas;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ReservaResponseDTO {

    private long id;
    private UUID clientId;
    private List<ItemReservaResponseDTO> itens;
    private String status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataExpiracao;
    private TransacaoPagamentoResponseDTO pagamento; // DTO aninhado para o pagamento

    public ReservaResponseDTO(Reservas reserva) {
        this.id = reserva.getId();
        this.clientId = reserva.getClient().getId();
        this.status = reserva.getStatus().name();
        this.dataCriacao = reserva.getDataCriacao();
        this.dataExpiracao = reserva.getDataExpiracao();
        // A conversão da lista de itens e do pagamento precisa ser feita em um serviço/mapper
    }
}