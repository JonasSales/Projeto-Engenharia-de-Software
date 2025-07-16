package com.br.projetoyaskara.dto.response;

import com.br.projetoyaskara.model.AvaliacoesEventos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvaliacaoResponseDTO {

    private UUID id;

    private UUID eventoId;

    private UUID clientUserId;

    private Integer nota;

    private String comentario;

    private LocalDateTime horaAvaliacao;

    public AvaliacaoResponseDTO(AvaliacoesEventos avaliacao) {
        this.id = avaliacao.getId();
        this.eventoId = avaliacao.getEvento().getId();
        this.clientUserId = avaliacao.getClientUser().getId();
        this.nota = avaliacao.getNota();
        this.comentario = avaliacao.getComentario();
    }

}