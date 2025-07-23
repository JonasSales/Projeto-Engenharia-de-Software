package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.request.EventoCreateRequestDTO;
import com.br.projetoyaskara.dto.request.EventoUpdateRequestDTO;
import com.br.projetoyaskara.dto.response.EventoResponseDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.model.clientuser.ClientUser;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.OrganizacaoRepository;
import com.br.projetoyaskara.repository.UserRepository;
import com.br.projetoyaskara.util.Haversine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventosService {

    private final EventosRepository eventosRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final UserRepository userRepository;

    public EventosService(
            EventosRepository eventosRepository,
            OrganizacaoRepository organizacaoRepository,
            UserRepository userRepository) {
        this.eventosRepository = eventosRepository;
        this.organizacaoRepository = organizacaoRepository;
        this.userRepository = userRepository;
    }

    private Eventos findEventoOrThrow(UUID eventoId) {
        return eventosRepository
                .findById(eventoId).orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado."));}

    private Organizacao findOrganizacaoOrThrow(UUID organizacaoId) {
        return organizacaoRepository
                .findById(organizacaoId).orElseThrow(() -> new ResourceNotFoundException("Organizaca não encontrada"));
    }

    private ClientUser findClientUserOrThrow(UUID clientUserId) {
        return userRepository
                .findById(clientUserId).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public ResponseEntity<EventoResponseDTO> cadastrarEvento(Authentication authentication, EventoCreateRequestDTO eventosDTO) {
            Organizacao organizacao = findOrganizacaoOrThrow(eventosDTO.getOrganizacaoId());
            UUID userId = userRepository.findIdByEmail(authentication.getName());

            if (!organizacao.getProprietario().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Eventos evento = new Eventos();

            evento.setName(eventosDTO.getName());
            evento.setOrganizacao(organizacao);
            evento.setDescricao(eventosDTO.getDescricao());
            evento.setDataInicio(eventosDTO.getDataInicio());
            evento.setDataFim(eventosDTO.getDataFim());
            evento.setFaixaEtaria(eventosDTO.getFaixaEtaria());
            evento.setStatus(eventosDTO.getStatus());
            Eventos eventoSalvo = eventosRepository.save(evento);

            return ResponseEntity.status(HttpStatus.CREATED).body(new EventoResponseDTO(eventoSalvo));
    }


    public ResponseEntity<EventoResponseDTO> atualizarEvento(Authentication authentication, EventoUpdateRequestDTO eventosDTO) {
            Eventos eventoAtualizado = findEventoOrThrow(eventosDTO.getId());
            Organizacao organizacao = findOrganizacaoOrThrow(eventoAtualizado.getOrganizacao().getId());
            UUID userId = userRepository.findIdByEmail(authentication.getName());

            if (!organizacao.getProprietario().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            eventoAtualizado.setName(eventosDTO.getName());
            eventoAtualizado.setDescricao(eventosDTO.getDescricao());
            eventoAtualizado.setDataInicio(eventosDTO.getDataInicio());
            eventoAtualizado.setDataFim(eventosDTO.getDataFim());
            eventoAtualizado.setStatus(eventosDTO.getStatus());
            eventoAtualizado.setFaixaEtaria(eventosDTO.getFaixaEtaria());

            eventosRepository.save(eventoAtualizado);
            return ResponseEntity
                    .status(HttpStatus.OK).body(new EventoResponseDTO(eventoAtualizado));
    }

    public ResponseEntity<Void> deletarEvento(Authentication authentication,UUID id) {
        Eventos evento = findEventoOrThrow(id);
        Organizacao organizacao = findOrganizacaoOrThrow(evento.getOrganizacao().getId());
        UUID userId = userRepository.findIdByEmail(authentication.getName());

        if (!organizacao.getProprietario().getId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        eventosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    public ResponseEntity<List<EventoResponseDTO>> listarEventos() {
        List<Eventos> listaDeEventos = eventosRepository.findAll();
        List<EventoResponseDTO> listaDto = converterLista(listaDeEventos);
        return ResponseEntity.status(HttpStatus.OK).body(listaDto);
    }

    public ResponseEntity<EventoResponseDTO> buscarEventoPorId(UUID id) {
            return ResponseEntity.status(HttpStatus.OK).body(new EventoResponseDTO(findEventoOrThrow(id)));
    }

    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorNomeDaOrganizacao(String nomeDaOrganizacao) {
        List<Eventos> listaDeEventos = eventosRepository.findAllByOrganizacao_Name(nomeDaOrganizacao);
        List<EventoResponseDTO> listaDto = converterLista(listaDeEventos);
        return ResponseEntity.status(HttpStatus.OK).body(listaDto);
    }

    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorDescricao(String descricao) {
            List<Eventos> listaDeEventos = eventosRepository.findAllByDescricaoContaining(descricao);
            List<EventoResponseDTO> listaDto = converterLista(listaDeEventos);
            return ResponseEntity.status(HttpStatus.OK).body(listaDto);
    }

    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorOrganizacaoId(UUID organizacaoId) {
        List<Eventos> listaDeEventos = eventosRepository.findAllByOrganizacao_Id(organizacaoId);
        List<EventoResponseDTO> listaDto = converterLista(listaDeEventos);
        return ResponseEntity.status(HttpStatus.OK).body(listaDto);
    }

    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorFaixaEtaria(String faixaEtaria) {
            Eventos.FaixaEtaria faixa = Eventos.FaixaEtaria.valueOf(faixaEtaria.toUpperCase());
            List<Eventos> eventos = eventosRepository.findAllByFaixaEtaria(faixa);
            List<EventoResponseDTO> listaDto = converterLista(eventos);
            return ResponseEntity.status(HttpStatus.OK).body(listaDto);
    }

    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorStatus(String status) {
            Eventos.Status statusEvento = Eventos.Status.valueOf(status.toUpperCase());
            List<Eventos> eventos = eventosRepository.findAllByStatus(statusEvento);
            List<EventoResponseDTO> listaDto = converterLista(eventos);
            return ResponseEntity.status(HttpStatus.OK).body(listaDto);
    }

    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorDistancia(Authentication authentication) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        ClientUser cliente = findClientUserOrThrow(clientId);

        List<Eventos> eventos = eventosRepository.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(eventos.stream()
                .map(evento -> {
                    double distancia = Haversine.calcularDistancia(
                            cliente.getEndereco().getLatitude(), cliente.getEndereco().getLongitude(),
                            evento.getOrganizacao().getEndereco().getLatitude(), evento.getOrganizacao().getEndereco().getLongitude()
                    );
                    EventoResponseDTO dto = new EventoResponseDTO(evento);
                    dto.setDistancia(distancia);
                    return dto;
                })
                .sorted(Comparator.comparing(EventoResponseDTO::getDistancia))
                .collect(Collectors.toList()));
    }

    private List<EventoResponseDTO> converterLista(List<Eventos> eventos){
        return eventos.stream()
                .map(EventoResponseDTO::new)
                .toList();
    }


}

