package com.br.projetoyaskara.dto;

import com.br.projetoyaskara.model.Endereco;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizacaoDTO {

    private UUID id;
    
    private UUID idProprietario;

    @NotBlank(message = "O nome da organização não pode estar em branco.")
    @Size(min = 2, max = 255, message = "O nome da organização deve ter entre 2 e 255 caracteres.")
    private String name;

    @Size(max = 1000, message = "A descrição da organização não pode ter mais de 1000 caracteres.")
    private String description;

    @NotBlank(message = "O CNPJ não pode estar em branco.")
    @Pattern(regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}", message = "Formato de CNPJ inválido. Use XX.XXX.XXX/YYYY-ZZ.")
    private String cnpj;

    @NotNull(message = "O endereço da organização não pode ser nulo.")
    @Valid
    private Endereco endereco;

}