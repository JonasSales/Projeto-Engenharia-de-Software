package com.br.projetoyaskara.dto;

import com.br.projetoyaskara.model.AvaliacoesEventos;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoEventosDTO {

    private Long id;
    private Long eventoId;
    private UUID clientUserId;
    private int nota;
    private String comentario;
    private LocalDateTime horaAvaliacao;

    public AvaliacaoEventosDTO(AvaliacoesEventos avaliacao) {
        setId(avaliacao.getId());
        setEventoId(avaliacao.getEvento().getId());
        setClientUserId(avaliacao.getClientUser().getId());
        setNota(avaliacao.getNota());
        setComentario(avaliacao.getComentario());
        setHoraAvaliacao(avaliacao.getHoraAvaliacao());
    }
}