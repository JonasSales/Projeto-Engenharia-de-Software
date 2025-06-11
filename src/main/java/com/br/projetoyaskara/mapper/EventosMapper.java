package com.br.projetoyaskara.mapper;

import com.br.projetoyaskara.dto.EventosDTO;
import com.br.projetoyaskara.model.Eventos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventosMapper {

    @Mapping(source = "organizacao.id", target = "organizacaoId")
    EventosDTO toDTO(Eventos eventos);

    List<EventosDTO> toDTO(List<Eventos> eventos);

    Eventos toEntity(EventosDTO eventosDTO);
}