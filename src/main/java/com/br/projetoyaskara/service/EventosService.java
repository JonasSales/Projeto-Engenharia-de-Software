package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.EventosDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.EventosMapper;
import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.OrganizacaoRepository;
import com.br.projetoyaskara.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventosService {

    private final EventosMapper eventosMapper;
    private final EventosRepository eventosRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final UserRepository userRepository;

    public EventosService(
            EventosMapper eventosMapper,
            EventosRepository eventosRepository,
            OrganizacaoRepository organizacaoRepository,
            UserRepository userRepository) {
        this.eventosMapper = eventosMapper;
        this.eventosRepository = eventosRepository;
        this.organizacaoRepository = organizacaoRepository;
        this.userRepository = userRepository;
    }

    private Eventos findEventoOrThrow(Long eventoId) {
        return eventosRepository
                .findById(eventoId).orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado."));}

    private Organizacao findOrganizacaoOrThrow(UUID organizacaoId) {
        return organizacaoRepository
                .findById(organizacaoId).orElseThrow(() -> new ResourceNotFoundException("Organizaca não encontrada"));
    }

    public ResponseEntity<EventosDTO> cadastrarEvento(Authentication authentication,EventosDTO eventosDTO) {
            Organizacao organizacao = findOrganizacaoOrThrow(eventosDTO.getOrganizacaoId());
            UUID userId = userRepository.findIdByEmail(authentication.getName());

            if (!organizacao.getProprietario().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Eventos evento = eventosMapper.toEntity(eventosDTO);
            evento.setOrganizacao(organizacao);
            Eventos eventoSalvo = eventosRepository.save(evento);

            return ResponseEntity.status(HttpStatus.CREATED).body(eventosMapper.toDTO(eventoSalvo));
    }


    public ResponseEntity<EventosDTO> atualizarEvento(Authentication authentication, EventosDTO eventosDTO) {
            Eventos eventoAtualizado = findEventoOrThrow(eventosDTO.getId());
            Organizacao organizacao = findOrganizacaoOrThrow(eventosDTO.getOrganizacaoId());
            UUID userId = userRepository.findIdByEmail(authentication.getName());

            if (!organizacao.getProprietario().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            eventoAtualizado.setName(eventosDTO.getName());
            eventoAtualizado.setDescricao(eventosDTO.getDescricao());
            eventoAtualizado.setDataInicio(eventosDTO.getDataInicio());
            eventoAtualizado.setDataFim(eventosDTO.getDataFim());
            eventoAtualizado.setFaixaEtaria(eventosDTO.getFaixaEtaria());
            eventoAtualizado.setStatus(eventosDTO.getStatus());
            return ResponseEntity
                    .status(HttpStatus.OK).body(eventosMapper.toDTO(eventosRepository.save(eventoAtualizado)));
    }

    public ResponseEntity<Void> deletarEvento(Authentication authentication,long id) {
        Eventos evento = findEventoOrThrow(id);
        Organizacao organizacao = findOrganizacaoOrThrow(evento.getOrganizacao().getId());
        UUID userId = userRepository.findIdByEmail(authentication.getName());

        if (!organizacao.getProprietario().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        eventosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<EventosDTO>> listarEventos() {
        return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventosRepository.findAll()));
    }

    public ResponseEntity<EventosDTO> buscarEventoPorId(long id) {
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(findEventoOrThrow(id)));
    }

    public ResponseEntity<List<EventosDTO>> buscarEventosPorNomeDaOrganizacao(String nomeDaOrganizacao) {
            List<Eventos> eventos = eventosRepository.findAllByOrganizacao_Name(nomeDaOrganizacao);
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
    }

    public ResponseEntity<?> buscarEventosPorDescricao(String descricao) {
            List<Eventos> eventos = eventosRepository.findAllByDescricaoContaining(descricao);
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
    }

    public ResponseEntity<List<EventosDTO>> buscarEventosPorOrganizacaoId(UUID organizacaoId) {
        findOrganizacaoOrThrow(organizacaoId);
        List<Eventos> eventos = eventosRepository.findAllByOrganizacao_Id(organizacaoId);
        return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
    }

    public ResponseEntity<List<EventosDTO>> buscarEventosPorFaixaEtaria(String faixaEtaria) {
            Eventos.FaixaEtaria faixa = Eventos.FaixaEtaria.valueOf(faixaEtaria.toUpperCase());
            List<Eventos> eventos = eventosRepository.findAllByFaixaEtaria(faixa);
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
    }

    public ResponseEntity<List<EventosDTO>> buscarEventosPorStatus(String status) {
            Eventos.Status statusEvento = Eventos.Status.valueOf(status.toUpperCase());
            List<Eventos> eventos = eventosRepository.findAllByStatus(statusEvento);
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
    }
}