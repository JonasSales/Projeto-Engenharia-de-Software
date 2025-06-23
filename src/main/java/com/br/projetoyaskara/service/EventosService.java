package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.EventosDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.EventosMapper;
import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.repository.EnderecoRepository;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.OrganizacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventosService {

    private final EventosMapper eventosMapper;
    private final EventosRepository eventosRepository;
    private final EnderecoRepository enderecoRepository;
    private final OrganizacaoRepository organizacaoRepository;

    public EventosService(
            EventosMapper eventosMapper, EventosRepository eventosRepository,
            EnderecoRepository enderecoRepository,
            OrganizacaoRepository organizacaoRepository) {
        this.eventosMapper = eventosMapper;
        this.eventosRepository = eventosRepository;
        this.enderecoRepository = enderecoRepository;
        this.organizacaoRepository = organizacaoRepository;
    }

    private Eventos findEventoOrThrow(Long eventoId) {
        return eventosRepository
                .findById(eventoId).orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado."));}

    private Organizacao findOrganizacaoOrThrow(UUID organizacaoId) {
        return organizacaoRepository
                .findById(organizacaoId).orElseThrow(() -> new ResourceNotFoundException("Organizaca não encontrada"));
    }

    public ResponseEntity<EventosDTO> cadastrarEvento(EventosDTO eventosDTO) {
            Organizacao organizacao = findOrganizacaoOrThrow(eventosDTO.getOrganizacaoId());
            if (eventosDTO.getEndereco() != null) {
                enderecoRepository.save(eventosDTO.getEndereco());
            }
            Eventos evento = eventosMapper.toEntity(eventosDTO);
            evento.setOrganizacao(organizacao);
            Eventos eventoSalvo = eventosRepository.save(evento);
            return ResponseEntity.status(HttpStatus.CREATED).body(eventosMapper.toDTO(eventoSalvo));
    }


    public ResponseEntity<EventosDTO> atualizarEvento(EventosDTO eventosDTO) {
            Eventos eventoAtualizado = findEventoOrThrow(eventosDTO.getId());

            eventoAtualizado.setName(eventosDTO.getName());
            eventoAtualizado.setDescricao(eventosDTO.getDescricao());
            eventoAtualizado.setDataInicio(eventosDTO.getDataInicio());
            eventoAtualizado.setDataFim(eventosDTO.getDataFim());
            eventoAtualizado.setFaixaEtaria(eventosDTO.getFaixaEtaria());
            eventoAtualizado.setStatus(eventosDTO.getStatus());
            return ResponseEntity
                    .status(HttpStatus.OK).body(eventosMapper.toDTO(eventosRepository.save(eventoAtualizado)));
    }

    public ResponseEntity<List<EventosDTO>> listarEventos() {
        List<Eventos> eventos = eventosRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
    }

    public ResponseEntity<Void> deletarEvento(long id) {
            findEventoOrThrow(id);
            eventosRepository.deleteById(id);
            return ResponseEntity.noContent().build();
    }

    public ResponseEntity<EventosDTO> buscarEventoPorId(long id) {
            Eventos eventos = findEventoOrThrow(id);
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
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