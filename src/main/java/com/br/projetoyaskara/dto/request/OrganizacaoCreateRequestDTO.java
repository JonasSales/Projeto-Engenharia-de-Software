package com.br.projetoyaskara.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizacaoCreateRequestDTO {

    @NotBlank(message = "O nome da organização não pode estar em branco.")
    @Size(min = 2, max = 255)
    private String name;

    @Size(max = 1000, message = "A descrição não pode ter mais de 1000 caracteres.")
    private String description;

    @NotBlank(message = "O CNPJ não pode estar em branco.")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "Formato de CNPJ inválido. Use XX.XXX.XXX/YYYY-ZZ.")
    private String cnpj;

    @NotNull(message = "O endereço é obrigatório.")
    @Valid
    private EnderecoRequestDTO endereco;
}
