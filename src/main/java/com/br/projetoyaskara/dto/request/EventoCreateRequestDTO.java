package com.br.projetoyaskara.dto.request;

import com.br.projetoyaskara.model.Eventos; // Supondo que os Enums estejam aqui
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter

public class EventoCreateRequestDTO {

    @NotBlank(message = "Nome do evento é obrigatório.")
    @Size(max = 255)
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

    @NotNull(message = "Status é obrigatário")
    private Eventos.Status status;

    @NotNull(message = "Organização é obrigatória.")
    private UUID organizacaoId;

    //@NotNull(message = "Endereço é obrigatório.")
    @Valid
    private EnderecoRequestDTO endereco;

}