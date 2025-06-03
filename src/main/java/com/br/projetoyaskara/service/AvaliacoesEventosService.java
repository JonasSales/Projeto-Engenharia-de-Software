package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.AvaliacaoEventosDTO;
import com.br.projetoyaskara.model.AvaliacoesEventos;
import com.br.projetoyaskara.model.ClientUser;
import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.repository.AvaliacoesEventosRepository;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AvaliacoesEventosService {

    private final AvaliacoesEventosRepository avaliacoesRepository;
    private final EventosRepository eventosRepository;
    private final UserRepository userRepository;

    public AvaliacoesEventosService(AvaliacoesEventosRepository avaliacoesRepository,
                                    EventosRepository eventosRepository,
                                    UserRepository userRepository) {
        this.avaliacoesRepository = avaliacoesRepository;
        this.eventosRepository = eventosRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> avaliacoesPorIdEvento(long eventoId) {
        List<AvaliacoesEventos> avaliacoesEventos = avaliacoesRepository.findAvaliacoesEventosByEventoId(eventoId);
        List<AvaliacaoEventosDTO> dtos = avaliacoesEventos.stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> avaliacoesPorClientUserId(UUID clientUserId) {
        List<AvaliacoesEventos> avaliacoesEventos = avaliacoesRepository.findAvaliacoesEventosByClientUserId(clientUserId);
        List<AvaliacaoEventosDTO> dtos = avaliacoesEventos.stream()
                .map(this::toDTO)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> save(AvaliacaoEventosDTO avaliacaoDTO) {
        // Verificar existência do evento
        Eventos evento = eventosRepository.findEventosById(avaliacaoDTO.getEventoId());
        if (evento == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento não encontrado.");
        }


        ClientUser client = userRepository.findClientUserById(avaliacaoDTO.getClientUserId());
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        AvaliacoesEventos avaliacao = new AvaliacoesEventos();
        avaliacao.setEvento(evento);
        avaliacao.setClientUser(client);
        avaliacao.setNota(avaliacaoDTO.getNota());
        avaliacao.setComentario(avaliacaoDTO.getComentario());

        AvaliacoesEventos saved = avaliacoesRepository.save(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    public ResponseEntity<?> update(AvaliacaoEventosDTO avaliacaoDTO) {
        AvaliacoesEventos avaliacao = avaliacoesRepository.findAvaliacoesEventosById(avaliacaoDTO.getId());
        if (avaliacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avaliação não encontrada.");
        }

        avaliacao.setNota(avaliacaoDTO.getNota());
        avaliacao.setComentario(avaliacaoDTO.getComentario());

        return ResponseEntity.status(HttpStatus.OK).body(toDTO(avaliacoesRepository.save(avaliacao)));
    }

    public ResponseEntity<?> deleteById(Long id) {
        if (!avaliacoesRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Avaliação não encontrada.");
        }
        avaliacoesRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private AvaliacaoEventosDTO toDTO(AvaliacoesEventos avaliacoesEventos) {
        return new AvaliacaoEventosDTO(avaliacoesEventos);
    }
}
