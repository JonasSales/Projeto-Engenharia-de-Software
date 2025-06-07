package com.br.projetoyaskara.dto;

import com.br.projetoyaskara.model.Endereco;
import com.br.projetoyaskara.model.Eventos;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class EventosDTO {

    private Long id;
    @NotBlank(message = "Nome do evento é obrigatório.")
    @Size(max = 255, message = "Nome do evento não pode exceder 255 caracteres.")
    private String name;
    @Size(max = 1000, message = "Descrição do evento não pode exceder 1000 caracteres.")
    private String descricao;
    @NotNull(message = "Data de início é obrigatória.")
    @FutureOrPresent(message = "Data de início não pode ser no passado.")
    private LocalDateTime dataInicio;
    @NotNull(message = "Data de fim é obrigatória.")
    @FutureOrPresent(message = "Data de fim não pode ser no passado.")
    private LocalDateTime dataFim;
    @NotNull(message = "Faixa etária é obrigatória.")
    private Eventos.FaixaEtaria faixaEtaria;
    @NotNull(message = "Status é obrigatório.")
    private Eventos.Status status;
    @NotNull(message = "Organização é obrigatória.")
    private UUID organizacaoId;
    @Valid
    @NotNull(message = "Endereço é obrigatório.")
    private Endereco endereco;

}
