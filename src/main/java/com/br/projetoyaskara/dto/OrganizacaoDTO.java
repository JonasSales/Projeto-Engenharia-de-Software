package com.br.projetoyaskara.dto;


import com.br.projetoyaskara.model.Organizacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OrganizacaoDTO {

    public OrganizacaoDTO(Organizacao organizacao) {
        setId(organizacao.getId());
        setName(organizacao.getName());
        setDescription(organizacao.getDescription());
        setEndereco(new EnderecoDTO(organizacao.getEndereco()));
        setCnpj(organizacao.getCnpj());
    }

    private UUID id;
    private String name;
    private String description;
    private String cnpj;
    private EnderecoDTO endereco;

}
