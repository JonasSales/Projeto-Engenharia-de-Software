package com.br.projetoyaskara.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_transacao_pagamento")
public class TransacaoPagamento {

    public enum MetodoPagamento {
        PIX,
        CARTAO_CREDITO,
        CARTAO_DEBITO
    }

    public enum StatusPagamento {
        PENDENTE,
        PAGO,
        FALHOU,
        REEMBOLSADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPagamento metodoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status;

    @Column(nullable = false)
    private int valorTotal;

    @Column(unique = true)
    private String transacaoGatewayId;

    @Lob
    private String qrCodePix;

    @Lob
    private String linkPagamento;

    private LocalDateTime dataDeCriacao;

    private LocalDateTime dataDePagamento;

    private LocalDateTime dataDeReembolso;

    @PrePersist
    protected void onCreate() {
        dataDeCriacao = LocalDateTime.now();
        status = StatusPagamento.PENDENTE;
    }
}