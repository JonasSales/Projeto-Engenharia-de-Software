package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.AvaliacaoEventosDTO;
import com.br.projetoyaskara.exception.BadRequestException;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.AvaliacoesEventosMapper;
import com.br.projetoyaskara.model.AvaliacoesEventos;
import com.br.projetoyaskara.repository.AvaliacoesEventosRepository;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class AvaliacoesEventosService {

    private final AvaliacoesEventosMapper avaliacoesEventosMapper;
    private final AvaliacoesEventosRepository avaliacoesRepository;
    private final EventosRepository eventosRepository;
    private final UserRepository userRepository;

    public AvaliacoesEventosService(AvaliacoesEventosMapper avaliacoesEventosMapper, AvaliacoesEventosRepository avaliacoesRepository,
                                    EventosRepository eventosRepository,
                                    UserRepository userRepository) {
        this.avaliacoesEventosMapper = avaliacoesEventosMapper;
        this.avaliacoesRepository = avaliacoesRepository;
        this.eventosRepository = eventosRepository;
        this.userRepository = userRepository;
    }

    // Método auxiliar para buscar evento, lançando exceção se não encontrado
    private void findEventoOrThrow(Long eventoId) {
        eventosRepository.findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado."));
    }

    // Método auxiliar para buscar usuário, lançando exceção se não encontrado
    private void findClientUserOrThrow(UUID clientUserId) {
        userRepository.findById(clientUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    // Método auxiliar para buscar avaliação, lançando exceção se não encontrada
    private AvaliacoesEventos findAvaliacaoOrThrow(Long avaliacaoId) {
        return avaliacoesRepository.findById(avaliacaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada."));
    }

    public ResponseEntity<?> avaliacoesPorIdEvento(long eventoId) {
        try {
            return ResponseEntity.ok(avaliacoesEventosMapper.toDTOList(avaliacoesRepository.findAvaliacoesEventosByEventoId(eventoId)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao buscar avaliações por ID de evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao buscar avaliações.");
        }
    }

    public ResponseEntity<?> avaliacoesPorClientUserId(UUID clientUserId) {
        try {
            findClientUserOrThrow(clientUserId);
            return ResponseEntity.ok(avaliacoesEventosMapper.toDTOList(avaliacoesRepository.findAvaliacoesEventosByClientUserId(clientUserId)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao buscar avaliações por ID de usuário: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao buscar avaliações.");
        }
    }

    public ResponseEntity<?> save(AvaliacaoEventosDTO avaliacaoDTO) {
        try {

            findEventoOrThrow(avaliacaoDTO.getEventoId());
            findClientUserOrThrow(avaliacaoDTO.getClientUserId());

            AvaliacoesEventos saved = avaliacoesRepository.save(avaliacoesEventosMapper.toEntity(avaliacaoDTO));
            return ResponseEntity.status(HttpStatus.CREATED).body(avaliacoesEventosMapper.toDTO(saved));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            System.err.println("Erro de violação de integridade ao salvar avaliação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Não foi possível salvar a avaliação devido a uma violação de dados (ex: avaliação duplicada para o mesmo evento/usuário).");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao salvar avaliação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao salvar avaliação.");
        }
    }

    public ResponseEntity<?> update(AvaliacaoEventosDTO avaliacaoDTO) {
        try {
            AvaliacoesEventos avaliacao = findAvaliacaoOrThrow(avaliacaoDTO.getId());
            findClientUserOrThrow(avaliacaoDTO.getClientUserId());
            avaliacao.setNota(avaliacaoDTO.getNota());
            avaliacao.setComentario(avaliacaoDTO.getComentario());
            avaliacao.setHoraAvaliacao(LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.OK).body(avaliacoesEventosMapper.toDTO(avaliacoesRepository.save(avaliacao)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar avaliação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao atualizar avaliação.");
        }
    }

    public ResponseEntity<?> deleteById(Long id) {
        try {
            findAvaliacaoOrThrow(id);
            avaliacoesRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao deletar avaliação: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao deletar avaliação.");
        }
    }

}