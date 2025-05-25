package com.br.projetoyaskara.model;


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
@Table(name = "tb_pagamentos")
public class Pagamentos {

    public enum MetodoPagamento {
        PIX,
        CREDITO,
        DEBITO
    }

    public enum Status {
        PAGO,
        PENDENTE,
        FALHOU
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id")
    private Reservas reserva;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    @Enumerated(EnumType.STRING)
    private Status status;

    private int totalPago;

    private String transacaoId;

    private LocalDateTime dataDePagamento;
}

