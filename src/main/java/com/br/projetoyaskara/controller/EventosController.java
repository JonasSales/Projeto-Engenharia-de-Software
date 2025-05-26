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
        return eventosService.cadastrarEvento(eventos);
    }

    @PostMapping("/update")
    public ResponseEntity<?> atualizarEvento( @RequestBody Eventos eventos) {
        return eventosService.atualizarEvento(eventos);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deletarEvento(@PathVariable long id) {
        return eventosService.deletarEvento(id);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllEventos() {
        return eventosService.listarEventos();
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<?> getEventoById(@PathVariable Long id) {
        return eventosService.buscarEventoPorId(id);
    }

    @GetMapping("/get/organizacao/nome/{nomeOrganizacao}")
    public ResponseEntity<?> getEventoByNomeOrganizacao(@PathVariable String nomeOrganizacao) {
        return eventosService.buscarEventosPorNomeDaOrganizacao(nomeOrganizacao);
    }

    @GetMapping("/get/descricao/{descEvento}")
    public ResponseEntity<?> getEventoByDescEvento(@PathVariable String descEvento) {
        return eventosService.buscarEventosPorDescricao(descEvento);
    }

    @GetMapping("/get/organizacao/id/{idOrganizacao}")
    public ResponseEntity<?> getEventoByIdOrganizacao(@PathVariable UUID idOrganizacao) {
        return eventosService.buscarEventosPorOrganizacaoId(idOrganizacao);
    }

    @GetMapping("/get/faixa/{faixaEtaria}")
    public ResponseEntity<?> getEventoByFaixaEtaria(@PathVariable String faixaEtaria) {
        return eventosService.buscarEventosPorFaixaEtaria(faixaEtaria);
    }

    @GetMapping("/get/status/{status}")
    public ResponseEntity<?> getEventoByStatus(@PathVariable String status) {
        return eventosService.buscarEventosPorStatus(status);
    }
}
