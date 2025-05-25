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
@Table(name = "tb_lotes_ingressos")
public class LotesIngresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Eventos evento;

    @Column(length = 100, nullable = false)
    private String name;

    private int valor; // se for em centavos, considere documentar isso

    private int totalIngressos;

    private int totalVendas;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;
}

