package com.br.projetoyaskara.dto;

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
public class EventosDTO {

    public EventosDTO(Eventos eventos) {
        setId(eventos.getId());
        setOrganizacaoId(eventos.getOrganizacao().getId());
        setDescricao(eventos.getDescricao());
        setDataInicio(eventos.getDataInicio());
        setDataFim(eventos.getDataFim());
        //setEndereco(eventos.getEndereco());
        setFaixaEtaria(eventos.getFaixaEtaria());
        setStatus(eventos.getStatus());

    }

    private Long id;

    private UUID organizacaoId;

    private String descricao;

    private LocalDateTime dataInicio;

    private LocalDateTime dataFim;

    private EnderecoDTO endereco;

    private Eventos.FaixaEtaria faixaEtaria;

    private Eventos.Status status;

}
