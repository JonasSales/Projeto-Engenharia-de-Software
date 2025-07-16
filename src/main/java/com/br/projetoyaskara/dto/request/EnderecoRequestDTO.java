package com.br.projetoyaskara.dto.request;

import com.br.projetoyaskara.model.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoRequestDTO {

    private Long id;

    @Size(max = 100, message = "O complemento não pode ter mais de 100 caracteres.")
    private String complemento;

    @NotBlank(message = "O bairro não pode estar em branco.")
    @Size(max = 100, message = "O bairro não pode ter mais de 100 caracteres.")
    private String bairro;

    @NotBlank(message = "A cidade não pode estar em branco.")
    @Size(max = 100, message = "A cidade não pode ter mais de 100 caracteres.")
    private String cidade;

    @NotBlank(message = "O estado não pode estar em branco.")
    @Size(max = 2, message = "O estado deve ter 2 caracteres (UF).")
    private String estado;

    @NotBlank(message = "O CEP não pode estar em branco.")
    private String cep;

    @NotNull(message = "Latitude é obrigatória")
    double latitude;

    @NotNull(message = "Longitude é obrigatário")
    double longitude;

}