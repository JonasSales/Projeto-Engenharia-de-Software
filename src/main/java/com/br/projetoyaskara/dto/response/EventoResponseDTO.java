package com.br.projetoyaskara.dto.response;

import com.br.projetoyaskara.model.Eventos;
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
public class EventoResponseDTO {

    public EventoResponseDTO(Eventos eventos) {
        this.id = eventos.getId();
        this.name = eventos.getName();
        this.descricao = eventos.getDescricao();
        this.dataInicio = eventos.getDataInicio();
        this.dataFim = eventos.getDataFim();
        this.faixaEtaria = eventos.getFaixaEtaria();
        this.status = eventos.getStatus();
        this.endereco = eventos.getEndereco() != null ? new EnderecoResponseDTO(eventos.getEndereco()) : null;

    }

    private UUID id;
    private String name;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Eventos.FaixaEtaria faixaEtaria;
    private Eventos.Status status;
    private EnderecoResponseDTO endereco;
    private OrganizacaoResponseDTO organizacao;

}