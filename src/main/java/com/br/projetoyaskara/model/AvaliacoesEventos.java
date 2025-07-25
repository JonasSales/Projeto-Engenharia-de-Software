package com.br.projetoyaskara.model;

import com.br.projetoyaskara.model.clientuser.ClientUser;
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
@Table(name = "tb_avaliacoes")
public class AvaliacoesEventos {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Eventos evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private ClientUser clientUser;

    @Column(name = "nota", nullable = false)
    private int nota;

    @Column(name = "comentario", length = 500)
    private String comentario;

    @Column(name = "hora_avaliacao", nullable = false)
    private LocalDateTime horaAvaliacao;

    @PrePersist
    protected void prePersist() {
        if (horaAvaliacao == null) {
            horaAvaliacao = LocalDateTime.now();
        }
    }
}


