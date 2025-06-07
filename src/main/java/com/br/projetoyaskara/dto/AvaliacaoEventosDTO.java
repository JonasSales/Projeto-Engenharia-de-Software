package com.br.projetoyaskara.dto;


import lombok.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoEventosDTO {

    private Long id;

    @NotNull(message = "O ID do evento não pode ser nulo.")
    private Long eventoId;

    @NotNull(message = "O ID do usuário não pode ser nulo.")
    private UUID clientUserId;

    @Min(value = 0, message = "A nota deve ser no mínimo 0.")
    @Max(value = 5, message = "A nota deve ser no máximo 5.")
    @NotNull(message = "A nota não pode ser nula.")
    private Integer nota;

    @Size(max = 500, message = "O comentário não pode ter mais de 500 caracteres.")
    private String comentario;

    private LocalDateTime horaAvaliacao;

}