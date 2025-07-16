package com.br.projetoyaskara.model;

import com.br.projetoyaskara.dto.request.EnderecoRequestDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private long id;

    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 9, message = "CEP deve ter 8 ou 9 caracteres")
    @Column(length = 9, nullable = false)
    private String cep;

    @NotBlank(message = "Cidade é obrigatória")
    @Column(length = 100, nullable = false)
    private String cidade;

    @NotNull(message = "Latitude é obrigatória")
    double latitude;

    @NotNull(message = "Longitude é obrigatário")
    double longitude;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve conter 2 caracteres")
    @Column(length = 2, nullable = false)
    private String estado;

    @NotBlank(message = "Bairro é obrigatório")
    @Column(length = 100, nullable = false)
    private String bairro;

    @Column(length = 200)
    private String complemento;

    public Endereco (EnderecoRequestDTO enderecoResponseDTO) {
        this.cep = enderecoResponseDTO.getCep();
        this.cidade = enderecoResponseDTO.getCidade();
        this.latitude = enderecoResponseDTO.getLatitude();
        this.longitude = enderecoResponseDTO.getLongitude();
        this.estado = enderecoResponseDTO.getEstado();
        this.bairro = enderecoResponseDTO.getBairro();
        this.complemento = enderecoResponseDTO.getComplemento();

    }
}

