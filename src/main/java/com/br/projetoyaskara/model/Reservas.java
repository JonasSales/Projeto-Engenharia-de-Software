package com.br.projetoyaskara.model;

import com.br.projetoyaskara.model.clientuser.ClientUser;
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
@Table(name = "tb_reservas")
public class Reservas {

    public enum Status {
        PENDENTE,
        PROCESSANDO,
        CONCLUIDA,
        CANCELADA,
        EXPIRADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientUser client;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemReserva> itens;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataExpiracao;

    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TransacaoPagamento transacaoPagamento;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataExpiracao = LocalDateTime.now().plusMinutes(15);
        status = Status.PENDENTE;
    }
}