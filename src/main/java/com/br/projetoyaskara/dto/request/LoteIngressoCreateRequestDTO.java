package com.br.projetoyaskara.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class LoteIngressoCreateRequestDTO {

    @NotBlank(message = "O nome do lote de ingresso não pode estar em branco.")
    @Size(min = 2, max = 100)
    private String name;

    @NotNull(message = "O ID do evento não pode ser nulo.")
    private UUID idEvento;

    @NotNull(message = "O total de ingressos não pode ser nulo.")
    @Min(value = 1, message = "O total de ingressos deve ser no mínimo 1.")
    private Long totalIngressos;

    @NotNull(message = "O valor não pode ser nulo.")
    @PositiveOrZero(message = "O valor deve ser um número não negativo.")
    private int valor;

    @NotNull(message = "A data de início da venda não pode ser nula.")
    @FutureOrPresent(message = "A data de início não pode ser no passado.")
    private LocalDateTime dataInicio;

    @NotNull(message = "A data de fim da venda não pode ser nula.")
    @FutureOrPresent(message = "A data de fim não pode ser no passado.")
    private LocalDateTime dataFim;
}