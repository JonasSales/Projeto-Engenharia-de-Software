package com.br.projetoyaskara.model;

import com.br.projetoyaskara.model.clientuser.ClientUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", nullable = false, unique = true)
    private Reservas reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientUser clientUser;

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