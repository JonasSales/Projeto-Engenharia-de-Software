package com.br.projetoyaskara.mapper;

import com.br.projetoyaskara.dto.EnderecoDTO;
import com.br.projetoyaskara.model.Endereco;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    EnderecoMapper INSTANCE = Mappers.getMapper(EnderecoMapper.class);

    EnderecoDTO toDto(Endereco entity);

    Endereco toEntity(EnderecoDTO dto);

}