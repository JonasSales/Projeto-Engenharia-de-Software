package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.AvaliacaoEventosDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.AvaliacoesEventosMapper;
import com.br.projetoyaskara.model.AvaliacoesEventos;
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

    private final AvaliacoesEventosMapper avaliacoesEventosMapper;
    private final AvaliacoesEventosRepository avaliacoesRepository;
    private final EventosRepository eventosRepository;
    private final UserRepository userRepository;

    public AvaliacoesEventosService(AvaliacoesEventosMapper avaliacoesEventosMapper,
                                    AvaliacoesEventosRepository avaliacoesRepository,
                                    EventosRepository eventosRepository,
                                    UserRepository userRepository) {
        this.avaliacoesEventosMapper = avaliacoesEventosMapper;
        this.avaliacoesRepository = avaliacoesRepository;
        this.eventosRepository = eventosRepository;
        this.userRepository = userRepository;
    }

    private void findEventoOrThrow(Long eventoId) {
        eventosRepository.findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado."));
    }

    private AvaliacoesEventos findAvaliacaoOrThrow(Long avaliacaoId) {
        return avaliacoesRepository.findById(avaliacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada."));
    }

    public ResponseEntity<List<AvaliacaoEventosDTO>> avaliacoesPorIdEvento(long eventoId) {
        List<AvaliacoesEventos> avaliacoes = avaliacoesRepository.findAvaliacoesEventosByEventoId(eventoId);
        return ResponseEntity.ok(avaliacoesEventosMapper.toDTOList(avaliacoes));
    }

    public ResponseEntity<List<AvaliacaoEventosDTO>> avaliacoesDoUsuarioDeUmEvento(Authentication authentication,long eventoId) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        findEventoOrThrow(eventoId);
        List<AvaliacoesEventos> avaliacoes = avaliacoesRepository.avaliacoesDoClientPorEvento(clientId, eventoId);
        return ResponseEntity.ok(avaliacoesEventosMapper.toDTOList(avaliacoes));
    }

    public ResponseEntity<List<AvaliacaoEventosDTO>> avaliacoesPorUser(Authentication authentication) {
        return ResponseEntity.ok(avaliacoesEventosMapper.
                toDTOList(avaliacoesRepository.findAvaliacoesEventosByClientUserEmail(authentication.getName())));
    }

    public ResponseEntity<AvaliacaoEventosDTO> save(Authentication authentication, AvaliacaoEventosDTO avaliacaoDTO) {

        findEventoOrThrow(avaliacaoDTO.getEventoId());
        AvaliacoesEventos avaliacaoSalva = avaliacoesEventosMapper.toEntity(avaliacaoDTO);
        avaliacaoSalva.setClientUser(userRepository.findByEmail(authentication.getName()));

        AvaliacoesEventos saved = avaliacoesRepository.save(avaliacaoSalva);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacoesEventosMapper.toDTO(saved));
    }

    public ResponseEntity<AvaliacaoEventosDTO> update(Authentication authentication,AvaliacaoEventosDTO avaliacaoDTO) {

        AvaliacoesEventos avaliacao = findAvaliacaoOrThrow(avaliacaoDTO.getId());
        UUID clientUserId = userRepository.findIdByEmail(authentication.getName());

        if (!clientUserId.equals(avaliacaoDTO.getClientUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        avaliacao.setNota(avaliacaoDTO.getNota());
        avaliacao.setComentario(avaliacaoDTO.getComentario());
        avaliacao.setHoraAvaliacao(LocalDateTime.now());

        return ResponseEntity
                .status(HttpStatus.OK).body(avaliacoesEventosMapper.toDTO(avaliacoesRepository.save(avaliacao)));
    }

    public ResponseEntity<Void> deleteById(Authentication authentication , Long id) {
            AvaliacoesEventos avaliacoesEventos = findAvaliacaoOrThrow(id);
            UUID clientUserId = userRepository.findIdByEmail(authentication.getName());
            if (!clientUserId.equals(avaliacoesEventos.getClientUser().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            avaliacoesRepository.deleteById(id);
            return ResponseEntity.noContent().build();
    }
}