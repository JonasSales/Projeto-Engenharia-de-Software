package com.br.projetoyaskara.service;


import com.br.projetoyaskara.dto.request.AvaliacaoRequestDTO;
import com.br.projetoyaskara.dto.response.AvaliacaoResponseDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.model.AvaliacoesEventos;
import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.model.clientuser.ClientUser;
import com.br.projetoyaskara.repository.AvaliacoesEventosRepository;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
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

    private Eventos findEventoOrThrow(UUID eventoId) {
        return eventosRepository
                .findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado."));
    }

    private AvaliacoesEventos findAvaliacaoOrThrow(UUID avaliacaoId) {
        return avaliacoesRepository
                .findById(avaliacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada."));
    }

    private ClientUser findClientUserOrThrow(UUID clientUserId) {
        return userRepository
                .findById(clientUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public ResponseEntity<List<AvaliacaoResponseDTO>> avaliacoesPorIdEvento(UUID eventoId) {
        List<AvaliacoesEventos> avaliacoes = avaliacoesRepository.findAvaliacoesEventosByEventoId(eventoId);
        List<AvaliacaoResponseDTO> listaDto = converterLista(avaliacoes);
        return ResponseEntity.ok(listaDto);
    }

    public ResponseEntity<List<AvaliacaoResponseDTO>> avaliacoesDoUsuarioDeUmEvento(Authentication authentication,UUID eventoId) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        findEventoOrThrow(eventoId);
        List<AvaliacoesEventos> avaliacoes = avaliacoesRepository.avaliacoesDoClientPorEvento(clientId, eventoId);
        List<AvaliacaoResponseDTO> listaDto = converterLista(avaliacoes);
        return ResponseEntity.ok(listaDto);
    }

    public ResponseEntity<List<AvaliacaoResponseDTO>> avaliacoesPorUser(Authentication authentication) {
        List<AvaliacoesEventos> avaliacoesEventos = avaliacoesRepository
                .findAvaliacoesEventosByClientUserEmail(authentication.getName());
        List<AvaliacaoResponseDTO> listaDto = converterLista(avaliacoesEventos);
        return ResponseEntity.ok(listaDto);
    }

    public ResponseEntity<AvaliacaoResponseDTO> save(Authentication authentication, @Valid AvaliacaoRequestDTO avaliacaoDTO) {

        Eventos eventos = findEventoOrThrow(avaliacaoDTO.getEventoId());
        UUID clienteId = userRepository.findIdByEmail(authentication.getName());
        ClientUser clientUser = findClientUserOrThrow(clienteId);
        AvaliacoesEventos avaliacao = new AvaliacoesEventos();
        avaliacao.setEvento(eventos);
        avaliacao.setClientUser(clientUser);

        avaliacao.setComentario(avaliacaoDTO.getComentario());
        avaliacao.setHoraAvaliacao(LocalDateTime.now());
        avaliacao.setNota(avaliacaoDTO.getNota());


        AvaliacoesEventos saved = avaliacoesRepository.save(avaliacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AvaliacaoResponseDTO(saved));
    }

    public ResponseEntity<AvaliacaoResponseDTO> update(Authentication authentication, @Valid AvaliacaoRequestDTO avaliacaoDTO) {

        AvaliacoesEventos avaliacao = findAvaliacaoOrThrow(avaliacaoDTO.getId());
        UUID clientUserId = userRepository.findIdByEmail(authentication.getName());

        if (!clientUserId.equals(avaliacaoDTO.getClientUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        avaliacao.setNota(avaliacaoDTO.getNota());
        avaliacao.setComentario(avaliacaoDTO.getComentario());
        avaliacao.setHoraAvaliacao(LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.OK).body(new AvaliacaoResponseDTO(avaliacao));
    }

    public ResponseEntity<Void> deleteById(Authentication authentication , UUID id) {
            AvaliacoesEventos avaliacoesEventos = findAvaliacaoOrThrow(id);
            UUID clientUserId = userRepository.findIdByEmail(authentication.getName());
            if (!clientUserId.equals(avaliacoesEventos.getClientUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            avaliacoesRepository.deleteById(id);
            return ResponseEntity.noContent().build();
    }

    private List<AvaliacaoResponseDTO> converterLista(List<AvaliacoesEventos> avaliacoes) {
        return avaliacoes.stream().map(AvaliacaoResponseDTO::new).toList();
    }
}