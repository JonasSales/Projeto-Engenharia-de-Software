package com.br.projetoyaskara.mapper;


import com.br.projetoyaskara.dto.AvaliacaoEventosDTO;
import com.br.projetoyaskara.model.AvaliacoesEventos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AvaliacoesEventosMapper {
    @Mapping(source = "evento.id", target = "eventoId")
    @Mapping(source = "clientUser.id", target = "clientUserId")
    AvaliacaoEventosDTO toDTO(AvaliacoesEventos avaliacaoEventos);

    @Mapping(source = "eventoId", target = "evento.id")
    @Mapping(source = "clientUserId", target = "clientUser.id")
    AvaliacoesEventos toEntity(AvaliacaoEventosDTO avaliacaoEventos);

    List<AvaliacaoEventosDTO> toDTOList(List<AvaliacoesEventos> avaliacoesEventos);

}