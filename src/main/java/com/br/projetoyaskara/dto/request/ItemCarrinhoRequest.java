package com.br.projetoyaskara.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrinhoRequest {

    @NotNull(message = "O ID do lote de ingresso é obrigatório.")
    private UUID lotesIngressoId;

    @Min(value = 1, message = "A quantidade deve ser no mínimo 1.")
    private int quantidade;

}