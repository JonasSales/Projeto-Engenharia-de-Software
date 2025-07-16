package com.br.projetoyaskara.dto.response;

import com.br.projetoyaskara.model.LotesIngresso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoteIngressoResponseDTO {

    private UUID id;
    private String name;
    private Long totalIngressos;
    private Long totalVendas;
    private int valor;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private EventoResponseDTO evento;

    public LoteIngressoResponseDTO(LotesIngresso lotesIngresso) {
        this.id = lotesIngresso.getId();
        this.name = lotesIngresso.getName();
        this.totalIngressos = lotesIngresso.getTotalIngressos();
        this.totalVendas = lotesIngresso.getTotalVendas();
        this.valor = lotesIngresso.getValor();
        this.dataInicio = lotesIngresso.getDataInicio();
        this.dataFim = lotesIngresso.getDataFim();
        this.evento = new EventoResponseDTO(lotesIngresso.getEvento());
    }


}