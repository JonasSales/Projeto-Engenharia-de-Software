package com.br.projetoyaskara.dto.response;

import com.br.projetoyaskara.model.Endereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoResponseDTO {

    private long id;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private double latitude;
    private double longitude;

    public EnderecoResponseDTO(Endereco endereco) {
        this.id = endereco.getId();
        this.latitude = endereco.getLatitude();
        this.longitude = endereco.getLongitude();
        this.bairro =  endereco.getBairro();
        this.cep = endereco.getCep();
        this.cidade = endereco.getCidade();
        this.estado = endereco.getEstado();
        this.complemento = endereco.getComplemento();
    }
}