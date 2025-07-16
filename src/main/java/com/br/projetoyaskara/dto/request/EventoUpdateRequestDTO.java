package com.br.projetoyaskara.dto.request;

import com.br.projetoyaskara.model.Eventos;
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
public class EventoUpdateRequestDTO {

    @NotNull
    private UUID id;

    //@NotNull
    private UUID idOrganizacao;

    @NotBlank(message = "Nome do evento é obrigatório.")
    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String descricao;

    @NotNull(message = "Data de início é obrigatória.")
    @FutureOrPresent
    private LocalDateTime dataInicio;

    @NotNull(message = "Data de fim é obrigatória.")
    @FutureOrPresent
    private LocalDateTime dataFim;

    @NotNull(message = "Faixa etária é obrigatória.")
    private Eventos.FaixaEtaria faixaEtaria;

    //@NotNull(message = "Endereço é obrigatório.")
    @Valid
    private EnderecoRequestDTO endereco;

}
