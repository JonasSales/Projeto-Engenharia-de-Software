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

    @PostMapping("/create")
    public ResponseEntity<?> cadastrarEvento( @RequestBody Eventos eventos) {
        return ResponseEntity.ok().body(eventosService.cadastrarEvento(eventos));
    }

    @PostMapping("/update")
    public ResponseEntity<?> atualizarEvento( @RequestBody Eventos eventos) {
        return ResponseEntity.ok().body(eventosService.atualizarEvento(eventos));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deletarEvento(@PathVariable long id) {
        return ResponseEntity.ok().body(eventosService.deletarEvento(id));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllEventos() {
        return ResponseEntity.ok().body(eventosService.listarEventos());
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<?> getEventoById(@PathVariable Long id) {
        return ResponseEntity.ok().body(eventosService.buscarEventoPorId(id));
    }

    @GetMapping("/get/organizacao/nome/{nomeOrganizacao}")
    public ResponseEntity<?> getEventoByNomeOrganizacao(@PathVariable String nomeOrganizacao) {
        return ResponseEntity.ok().body(eventosService.buscarEventosPorNomeDaOrganizacao(nomeOrganizacao));
    }

    @GetMapping("/get/descricao/{descEvento}")
    public ResponseEntity<?> getEventoByDescEvento(@PathVariable String descEvento) {
        return ResponseEntity.ok().body(eventosService.buscarEventosPorDescricao(descEvento));
    }

    @GetMapping("/get/organizacao/id/{idOrganizacao}")
    public ResponseEntity<?> getEventoByIdOrganizacao(@PathVariable UUID idOrganizacao) {
        return ResponseEntity.ok().body(eventosService.buscarEventosPorOrganizacaoId(idOrganizacao));
    }

    @GetMapping("/get/faixa/{faixaEtaria}")
    public ResponseEntity<?> getEventoByFaixaEtaria(@PathVariable String faixaEtaria) {
        return ResponseEntity.ok().body(eventosService.buscarEventosPorFaixaEtaria(faixaEtaria));
    }

    @GetMapping("/get/status/{status}")
    public ResponseEntity<?> getEventoByStatus(@PathVariable String status) {
        return ResponseEntity.ok().body(eventosService.buscarEventosPorStatus(status));
    }
}
