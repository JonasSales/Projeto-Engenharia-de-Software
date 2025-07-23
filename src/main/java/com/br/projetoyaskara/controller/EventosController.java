package com.br.projetoyaskara.controller;


import com.br.projetoyaskara.dto.request.EventoCreateRequestDTO;
import com.br.projetoyaskara.dto.request.EventoUpdateRequestDTO;
import com.br.projetoyaskara.dto.response.EventoResponseDTO;
import com.br.projetoyaskara.service.EventosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eventos")
public class EventosController {

    private final EventosService eventosService;

    public EventosController(EventosService eventosService) {
        this.eventosService = eventosService;
    }

    @PreAuthorize("hasAnyAuthority('organization::create')")
    @PostMapping
    public ResponseEntity<EventoResponseDTO> cadastrarEvento(Authentication authentication,
                                                             @Valid @RequestBody EventoCreateRequestDTO eventosDTO) {
        return eventosService.cadastrarEvento(authentication, eventosDTO);
    }

    @PreAuthorize("hasAnyAuthority('organization::put')")
    @PutMapping
    public ResponseEntity<EventoResponseDTO> atualizarEvento(Authentication authentication,
                                             @Valid @RequestBody EventoUpdateRequestDTO eventosDTO) {
        return eventosService.atualizarEvento(authentication, eventosDTO);
    }

    @GetMapping
    public ResponseEntity<List<EventoResponseDTO>> listarEventos() {
        return eventosService.listarEventos();
    }

    @PreAuthorize("hasAnyAuthority('organization::delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEvento(Authentication authentication,@PathVariable UUID id) {
        return eventosService.deletarEvento(authentication, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> buscarEventoPorId(@PathVariable UUID id) {
        return eventosService.buscarEventoPorId(id);
    }

    @GetMapping("/organizacao/nome/{nomeDaOrganizacao}")
    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorNomeDaOrganizacao(@PathVariable String nomeDaOrganizacao) {
        return eventosService.buscarEventosPorNomeDaOrganizacao(nomeDaOrganizacao);
    }

    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorDescricao(@PathVariable String descricao) {
        return eventosService.buscarEventosPorDescricao(descricao);
    }

    @GetMapping("/organizacao/id/{organizacaoId}")
    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorOrganizacaoId(@PathVariable UUID organizacaoId) {
        return eventosService.buscarEventosPorOrganizacaoId(organizacaoId);
    }

    @GetMapping("/faixa-etaria/{faixaEtaria}")
    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorFaixaEtaria(@PathVariable String faixaEtaria) {
        return eventosService.buscarEventosPorFaixaEtaria(faixaEtaria);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorStatus(@PathVariable String status) {
        return eventosService.buscarEventosPorStatus(status);
    }

    @GetMapping("/distancia")
    public ResponseEntity<List<EventoResponseDTO>> buscarEventosPorDistancia(Authentication authentication) {
        return eventosService.buscarEventosPorDistancia(authentication);
    }
}