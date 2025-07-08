package com.br.projetoyaskara.dto;

import com.br.projetoyaskara.model.TransacaoPagamento;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransacaoPagamentoResponseDTO {

    private Long id;
    private String status;
    private String metodoPagamento;
    private int valorTotal;
    private String qrCodePix;
    private String linkPagamento;
    private LocalDateTime dataDeCriacao;
    private LocalDateTime dataDePagamento;

    public TransacaoPagamentoResponseDTO(TransacaoPagamento transacao) {
        this.id = transacao.getId();
        this.status = transacao.getStatus().name();
        this.metodoPagamento = transacao.getMetodoPagamento().name();
        this.valorTotal = transacao.getValorTotal();
        this.qrCodePix = transacao.getQrCodePix();
        this.linkPagamento = transacao.getLinkPagamento();
        this.dataDeCriacao = transacao.getDataDeCriacao();
        this.dataDePagamento = transacao.getDataDePagamento();
    }
}