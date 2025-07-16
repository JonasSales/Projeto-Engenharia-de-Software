package com.br.projetoyaskara.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lote_ingresso_id")
    private LotesIngresso lotesIngresso;

    @Column(nullable = false)
    private String nomeIngresso;

    @Column(nullable = false)
    private int valor;

    public ItemPedido(Pedido pedido, LotesIngresso lote, int valor) {
        this.pedido = pedido;
        this.lotesIngresso = lote;
        this.nomeIngresso = lote.getName();
        this.valor = valor;
    }
}