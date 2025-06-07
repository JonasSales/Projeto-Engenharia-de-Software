package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.EventosDTO;
import com.br.projetoyaskara.service.EventosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/eventos")
public class EventosController {

    private final EventosService eventosService;

    public EventosController(EventosService eventosService) {
        this.eventosService = eventosService;
    }

    @PostMapping
    public ResponseEntity<?> cadastrarEvento(@Valid @RequestBody EventosDTO eventosDTO) {
        return eventosService.cadastrarEvento(eventosDTO);
    }

    @PutMapping
    public ResponseEntity<?> atualizarEvento(@Valid @RequestBody EventosDTO eventosDTO) {
        return eventosService.atualizarEvento(eventosDTO);
    }

    @GetMapping
    public ResponseEntity<?> listarEventos() {
        return eventosService.listarEventos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEvento(@PathVariable long id) {
        return eventosService.deletarEvento(id);
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
    public ResponseEntity<?> buscarEventosPorOrganizacaoId(@PathVariable UUID organizacaoId) {
        return eventosService.buscarEventosPorOrganizacaoId(organizacaoId);
    }

    @GetMapping("/faixa-etaria/{faixaEtaria}")
    public ResponseEntity<?> buscarEventosPorFaixaEtaria(@PathVariable String faixaEtaria) {
        return eventosService.buscarEventosPorFaixaEtaria(faixaEtaria);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> buscarEventosPorStatus(@PathVariable String status) {
        return eventosService.buscarEventosPorStatus(status);
    }

    @GetMapping("/{eventoId}/nota-media")
    public ResponseEntity<?> notaMediaEvento(@PathVariable long eventoId) {
        return eventosService.notaMediaEvento(eventoId);
    }
}