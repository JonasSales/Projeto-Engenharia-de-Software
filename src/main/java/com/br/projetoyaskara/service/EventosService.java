package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.EventosDTO;
import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.repository.EventosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventosService {

    private final EventosRepository eventosRepository;

    public EventosService(EventosRepository eventosRepository) {
        this.eventosRepository = eventosRepository;
    }

    public ResponseEntity<?> cadastrarEvento(Eventos eventos) {
        return new ResponseEntity<>(eventosRepository.save(eventos), HttpStatus.CREATED);
    }

    public ResponseEntity<?> listarEventos() {
        List<Eventos> eventos = eventosRepository.findAll();
        List<EventosDTO> dtos = eventos.stream()
                .map(EventosDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> buscarEventoPorId(long id) {
        return ResponseEntity.ok().body(new EventosDTO(eventosRepository.findEventosById(id)));
    }

    public ResponseEntity<?> buscarEventosPorNomeDaOrganizacao(String nomeDaOrganizacao) {
        List<Eventos> eventos = eventosRepository.findAllByOrganizacao_Name(nomeDaOrganizacao);
        List<EventosDTO> dtos = eventos.stream()
                .map(EventosDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }


    public ResponseEntity<?> buscarEventosPorDescricao(String descricao) {
        List<Eventos> eventos = eventosRepository.findAllByDescricaoContaining(descricao);
        List<EventosDTO> dtos = eventos.stream()
                .map(EventosDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> buscarEventosPorOrganizacaoId(UUID organizacaoId) {
        List<Eventos> eventos = eventosRepository.findAllByOrganizacao_Id(organizacaoId);
        List<EventosDTO> dtos = eventos.stream()
                .map(EventosDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> buscarEventosPorFaixaEtaria(String faixaEtaria) {
        List<Eventos> eventos = eventosRepository.findAllByFaixaEtaria(Eventos.FaixaEtaria.valueOf(faixaEtaria));
        List<EventosDTO> dtos = eventos.stream()
                .map(EventosDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> buscarEventosPorStatus(String status) {
        List<Eventos> eventos = eventosRepository.findAllByStatus(Eventos.Status.valueOf(status));
        List<EventosDTO> dtos = eventos.stream()
                .map(EventosDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

}
