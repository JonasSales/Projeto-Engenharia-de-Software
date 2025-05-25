package com.br.projetoyaskara.dto;

import com.br.projetoyaskara.model.Endereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EnderecoDTO {

    public EnderecoDTO(Endereco endereco) {
        setId(endereco.getId());
        setCidade(endereco.getCidade());
        setEstado(endereco.getEstado());
        setBairro(endereco.getBairro());
        setCep(endereco.getCep());
        setComplemento(endereco.getComplemento());
    }

    private long id;

    private String cep;

    private String cidade;

    private String estado;

    private String bairro;

    private String complemento;




}
