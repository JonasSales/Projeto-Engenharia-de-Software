package com.br.projetoyaskara.mapper;


import com.br.projetoyaskara.dto.OrganizacaoDTO;
import com.br.projetoyaskara.model.Organizacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface OrganizacaoMapper {

    @Mapping(source = "proprietario.id", target = "idProprietario")
    OrganizacaoDTO toDTO(Organizacao organizacao);

    List<OrganizacaoDTO> toDTO(List<Organizacao> organizacoes);

}