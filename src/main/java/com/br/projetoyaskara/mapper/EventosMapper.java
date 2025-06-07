package com.br.projetoyaskara.mapper;

import com.br.projetoyaskara.dto.EventosDTO;
import com.br.projetoyaskara.model.Eventos;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventosMapper {

    EventosDTO eventosToEventosDTO(Eventos eventos);

    List<EventosDTO> eventosToEventosDTO(List<Eventos> eventos);

    Eventos eventosDTOToEventos(EventosDTO eventosDTO);
}