package com.br.projetoyaskara.mapper;

import com.br.projetoyaskara.dto.ClientUserDTO;
import com.br.projetoyaskara.model.ClientUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientUserMapper {

    ClientUserDTO clientUserToClientUserDTO(ClientUser clientUser);
}