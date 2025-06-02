package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.service.EventosService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/eventos")
public class EventosController {

    private final EventosService eventosService;

    public EventosController(EventosService eventosService) {
        this.eventosService = eventosService;
    }

    @PostMapping()
    public ResponseEntity<?> cadastrarEvento( @RequestBody Eventos eventos) {
        return ResponseEntity.ok().body(eventosService.cadastrarEvento(eventos));
    }

    @PutMapping()
    public ResponseEntity<?> atualizarEvento( @RequestBody Eventos eventos) {
        return ResponseEntity.ok().body(eventosService.atualizarEvento(eventos));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEvento(@PathVariable long id) {
        return ResponseEntity.ok().body(eventosService.deletarEvento(id));
    }

    @GetMapping()
    public ResponseEntity<?> getAllEventos() {
        return ResponseEntity.ok().body(eventosService.listarEventos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventoById(@PathVariable Long id) {
        return ResponseEntity.ok().body(eventosService.buscarEventoPorId(id));
    }

    @GetMapping("/organizacao/name/{nomeOrganizacao}")
    public ResponseEntity<?> getEventoByNomeOrganizacao(@PathVariable String nomeOrganizacao) {
        return ResponseEntity.ok().body(eventosService.buscarEventosPorNomeDaOrganizacao(nomeOrganizacao));
    }

    @GetMapping("/descricao/{descEvento}")
    public ResponseEntity<?> getEventoByDescEvento(@PathVariable String descEvento) {
        return ResponseEntity.ok().body(eventosService.buscarEventosPorDescricao(descEvento));
    }

    @GetMapping("/organizacao/id/{idOrganizacao}")
    public ResponseEntity<?> getEventoByIdOrganizacao(@PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok().body(eventosService.buscarEventosPorOrganizacaoId(idOrganizacao));
    }

    @GetMapping("/faixa/{faixaEtaria}")
    public ResponseEntity<?> getEventoByFaixaEtaria(@PathVariable String faixaEtaria) {
        return ResponseEntity.ok().body(eventosService.buscarEventosPorFaixaEtaria(faixaEtaria));
    }

    @GetMapping("/get/status/{status}")
    public ResponseEntity<?> getEventoByStatus(@PathVariable String status) {
        return ResponseEntity.ok().body(eventosService.buscarEventosPorStatus(status));
    }
}
