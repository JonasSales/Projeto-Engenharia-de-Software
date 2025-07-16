package com.br.projetoyaskara.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class LoteIngressoUpdateRequestDTO {

    @NotNull
    UUID id;

    @NotBlank(message = "O nome do lote não pode estar em branco.")
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    @Min(value = 1, message = "O total de ingressos deve ser no mínimo 1.")
    private Long totalIngressos;

    @NotNull
    @PositiveOrZero(message = "O valor deve ser um número não negativo.")
    private int valor;

    @NotNull
    @FutureOrPresent
    private LocalDateTime dataInicio;

    @NotNull
    @FutureOrPresent
    private LocalDateTime dataFim;
}