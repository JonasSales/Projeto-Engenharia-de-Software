package com.br.projetoyaskara.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
public class AvaliacaoRequestDTO {

    @NotNull
    UUID id;

    @NotNull(message = "O ID do evento não pode ser nulo.")
    private UUID eventoId;

    @NotNull(message = "O ID do usuário não pode ser nulo.")
    private UUID clientUserId;

    @NotNull(message = "A nota não pode ser nula.")
    @Min(value = 0, message = "A nota deve ser no mínimo 0.")
    @Max(value = 5, message = "A nota deve ser no máximo 5.")
    private Integer nota;

    @Size(max = 500, message = "O comentário não pode ter mais de 500 caracteres.")
    private String comentario;


}