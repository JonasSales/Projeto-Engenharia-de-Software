package com.br.projetoyaskara.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemReservaRequestDTO {

    @NotNull(message = "O ID do lote de ingresso não pode ser nulo.")
    private Long loteIngressoId;

    @Min(value = 1, message = "A quantidade deve ser de no mínimo 1.")
    private int quantidade;
}