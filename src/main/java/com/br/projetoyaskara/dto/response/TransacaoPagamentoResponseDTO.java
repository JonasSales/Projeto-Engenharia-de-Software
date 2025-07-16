package com.br.projetoyaskara.dto.response;

import com.br.projetoyaskara.model.TransacaoPagamento;
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

    public TransacaoPagamentoResponseDTO(TransacaoPagamento transacaoPagamento) {
        this.id = transacaoPagamento.getId();
        this.status = String.valueOf(transacaoPagamento.getStatus());
        this.metodoPagamento = String.valueOf(transacaoPagamento.getMetodoPagamento());
        this.valorTotal = transacaoPagamento.getValorTotal();
        this.qrCodePix = transacaoPagamento.getQrCodePix();
        this.linkPagamento = transacaoPagamento.getLinkPagamento();
        this.dataDeCriacao = transacaoPagamento.getDataDeCriacao();
        this.dataDePagamento = transacaoPagamento.getDataDePagamento();
    }
}