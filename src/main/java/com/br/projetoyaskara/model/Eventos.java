package com.br.projetoyaskara.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tb_eventos")
public class Eventos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "organizacao_id")
    private Organizacao organizacao;

    @Column(length = 500)
    private String descricao;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY)
    private List<AvaliacoesEventos> avaliacoes;

    @OneToOne(mappedBy = "evento", fetch = FetchType.LAZY)
    private LotesIngresso lotesIngresso;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    private FaixaEtaria faixaEtaria;

    @Enumerated(EnumType.STRING)
    private Status status;

    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
    }

    public enum FaixaEtaria {
        CRIANCAS,
        ADOLESCENTES,
        ADULTOS,
        PUBLICO
    }

    public enum Status {
        ATIVO,
        ENCERRADO,
        CANCELADO
    }
}

