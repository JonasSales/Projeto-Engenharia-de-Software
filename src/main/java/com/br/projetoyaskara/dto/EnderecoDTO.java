package com.br.projetoyaskara.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoDTO {

    private Long id;

    @NotBlank(message = "O logradouro não pode estar em branco.")
    @Size(max = 255, message = "O logradouro não pode ter mais de 255 caracteres.")
    private String logradouro;

    @NotBlank(message = "O número não pode estar em branco.")
    @Size(max = 20, message = "O número não pode ter mais de 20 caracteres.")
    private String numero;

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
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Formato de CEP inválido. Use XXXXX-YYY.")
    private String cep;

    @NotNull(message = "Latitude é obrigatória")
    double latitude;

    @NotNull(message = "Longitude é obrigatário")
    double longitude;

}