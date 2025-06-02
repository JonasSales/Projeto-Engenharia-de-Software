package com.br.projetoyaskara.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_item_reserva")
public class ItemReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id", nullable = false)
    private Reservas reserva;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_ingresso_id", nullable = false)
    private LotesIngresso loteIngresso;

    private int quantidade;
}

