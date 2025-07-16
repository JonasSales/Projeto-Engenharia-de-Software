package com.br.projetoyaskara.dto.response;

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
public class OrganizacaoResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private String cnpj;
    private EnderecoResponseDTO endereco;
    private UserResponseDTO proprietario;

    public OrganizacaoResponseDTO(Organizacao organizacaoSalva) {
        this.id = organizacaoSalva.getId();
        this.name = organizacaoSalva.getName();
        this.description = organizacaoSalva.getDescription();
        this.cnpj = organizacaoSalva.getCnpj();
        this.endereco = organizacaoSalva.getEndereco() != null ? new EnderecoResponseDTO(organizacaoSalva.getEndereco()) : null;
        this.proprietario = new UserResponseDTO(organizacaoSalva.getProprietario());
    }
}