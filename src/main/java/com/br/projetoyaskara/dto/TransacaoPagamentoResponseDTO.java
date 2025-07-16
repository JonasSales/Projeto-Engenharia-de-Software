package com.br.projetoyaskara.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoPagamentoResponseDTO {

    private UUID id;
    private String status;
    private String metodoPagamento;
    private int valorTotal;
    private String qrCodePix;
    private String linkPagamento;
    private LocalDateTime dataDeCriacao;
    private LocalDateTime dataDePagamento;

}