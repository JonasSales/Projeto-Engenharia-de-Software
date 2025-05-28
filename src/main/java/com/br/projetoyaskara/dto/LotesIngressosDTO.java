package com.br.projetoyaskara.dto;

import com.br.projetoyaskara.model.LotesIngresso;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class LotesIngressosDTO {

    public LotesIngressosDTO (LotesIngresso lotesIngresso) {
        setId(lotesIngresso.getId());
        setName(lotesIngresso.getName());
        setIdEvento(lotesIngresso.getEvento().getId());
        setTotalIngressos(lotesIngresso.getTotalIngressos());
        setTotalIngressos(lotesIngresso.getTotalIngressos());
        setDataInicio(lotesIngresso.getDataInicio());
        setDataFim(lotesIngresso.getDataFim());
    }

    private long id;
    private String name;
    private long idEvento;
    private long totalIngressos;
    private long quantidadeVendida;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
}
