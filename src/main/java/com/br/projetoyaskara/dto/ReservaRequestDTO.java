package com.br.projetoyaskara.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReservaRequestDTO {

    @NotEmpty(message = "A lista de itens não pode ser vazia.")
    @Valid
    private List<ItemReservaRequestDTO> itens;
}