package com.br.projetoyaskara.mapper;

import com.br.projetoyaskara.dto.LotesIngressoDTO;
import com.br.projetoyaskara.model.LotesIngresso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LotesIngressoMapper {

    @Mapping(source = "evento.id", target = "idEvento")

    LotesIngressoDTO toDto(LotesIngresso lotesIngresso);

    List<LotesIngressoDTO> toDto(List<LotesIngresso> lotesIngressoList);

    LotesIngresso toEntity(LotesIngressoDTO lotesIngressoDTO);

}