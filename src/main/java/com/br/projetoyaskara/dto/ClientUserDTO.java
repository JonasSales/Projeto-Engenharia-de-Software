package com.br.projetoyaskara.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientUserDTO {

    private UUID id;

    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String name;

    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "Formato de e-mail inválido.")
    @Size(max = 255, message = "O e-mail não pode ter mais de 255 caracteres.")
    private String email;

    @NotBlank(message = "A função (role) não pode estar em branco.")
    private String role;

    @NotNull(message = "A data de criação não pode ser nula.")
    @PastOrPresent(message = "A data de criação não pode ser no futuro.")
    private LocalDateTime created;

    @NotNull(message = "A data de modificação não pode ser nula.")
    @PastOrPresent(message = "A data de modificação não pode ser no futuro.")
    private LocalDateTime modified;

    @NotNull(message = "O status de atividade não pode ser nulo.")
    private boolean active;

    private EnderecoDTO endereco;

}