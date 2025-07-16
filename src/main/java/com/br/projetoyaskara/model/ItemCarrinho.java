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
@Table(name = "item_carrinho")
public class ItemCarrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "lote_ingresso_id")
    private LotesIngresso lotesIngresso;

    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "carrinho_id")
    private Carrinho carrinho;

}
