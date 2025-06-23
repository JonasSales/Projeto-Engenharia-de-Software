package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.EventosDTO;
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
    public ResponseEntity<?> cadastrarEvento(Authentication authentication,
                                             @Valid @RequestBody EventosDTO eventosDTO) {
        return eventosService.cadastrarEvento(authentication, eventosDTO);
    }

    @PreAuthorize("hasAnyAuthority('organization::put')")
    @PutMapping
    public ResponseEntity<?> atualizarEvento(Authentication authentication,
                                             @Valid @RequestBody EventosDTO eventosDTO) {
        return eventosService.atualizarEvento(authentication, eventosDTO);
    }

    @GetMapping
    public ResponseEntity<?> listarEventos() {
        return eventosService.listarEventos();
    }

    @PreAuthorize("hasAnyAuthority('organization::delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEvento(Authentication authentication,@PathVariable long id) {
        return eventosService.deletarEvento(authentication, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEventoPorId(@PathVariable long id) {
        return eventosService.buscarEventoPorId(id);
    }

    @GetMapping("/organizacao/nome/{nomeDaOrganizacao}")
    public ResponseEntity<?> buscarEventosPorNomeDaOrganizacao(@PathVariable String nomeDaOrganizacao) {
        return eventosService.buscarEventosPorNomeDaOrganizacao(nomeDaOrganizacao);
    }

    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<?> buscarEventosPorDescricao(@PathVariable String descricao) {
        return eventosService.buscarEventosPorDescricao(descricao);
    }

    @GetMapping("/organizacao/id/{organizacaoId}")
    public ResponseEntity<List<EventosDTO>> buscarEventosPorOrganizacaoId(@PathVariable UUID organizacaoId) {
        return eventosService.buscarEventosPorOrganizacaoId(organizacaoId);
    }

    @GetMapping("/faixa-etaria/{faixaEtaria}")
    public ResponseEntity<?> buscarEventosPorFaixaEtaria(@PathVariable String faixaEtaria) {
        return eventosService.buscarEventosPorFaixaEtaria(faixaEtaria);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EventosDTO>> buscarEventosPorStatus(@PathVariable String status) {
        return eventosService.buscarEventosPorStatus(status);
    }
}