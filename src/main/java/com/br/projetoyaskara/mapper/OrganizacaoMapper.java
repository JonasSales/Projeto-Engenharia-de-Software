package com.br.projetoyaskara.mapper;


import com.br.projetoyaskara.dto.OrganizacaoDTO;
import com.br.projetoyaskara.model.Organizacao;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface OrganizacaoMapper {

    OrganizacaoDTO toDTO(Organizacao organizacao);

    List<OrganizacaoDTO> toDTO(List<Organizacao> organizacoes);

}