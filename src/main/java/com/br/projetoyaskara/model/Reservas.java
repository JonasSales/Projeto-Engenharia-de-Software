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
@Table(name = "tb_reservas")
public class Reservas {

    public enum Status {
        RESERVADO,
        PAGO,
        CANCELADO,
        EXPIRADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientUser client;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lotes_ingresso_id")
    private LotesIngresso lotesIngresso;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime dataReserva;
}

