package com.br.projetoyaskara.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemReservaResponseDTO {

    private Long id;
    private Long loteIngressoId;
    private String nomeLote;
    private int quantidade;
    private int valorUnitario;
}