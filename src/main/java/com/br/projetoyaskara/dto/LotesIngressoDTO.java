package com.br.projetoyaskara.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LotesIngressoDTO {

    private long id;

    @NotBlank(message = "O nome do lote de ingresso não pode estar em branco.")
    @Size(min = 2, max = 100, message = "O nome do lote de ingresso deve ter entre 2 e 100 caracteres.")
    private String name;

    @NotNull(message = "O ID do evento não pode ser nulo.")
    private long idEvento;

    @Min(value = 1, message = "O total de ingressos deve ser no mínimo 1.")
    private long totalIngressos;

    private long totalVendas;

    @Min(value = 0, message = "O valor deve ser um número não negativo.")
    private int valor;

    @NotNull(message = "A data de início não pode ser nula.")
    @FutureOrPresent(message = "A data de início não pode ser no passado.")
    private LocalDateTime dataInicio;

    @NotNull(message = "A data de fim não pode ser nula.")
    @FutureOrPresent(message = "A data de fim não pode ser no passado.")
    private LocalDateTime dataFim;

}