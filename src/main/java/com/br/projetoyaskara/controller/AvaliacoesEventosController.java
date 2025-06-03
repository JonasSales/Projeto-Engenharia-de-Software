package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.AvaliacaoEventosDTO;
import com.br.projetoyaskara.service.AvaliacoesEventosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequestMapping("/avaliacoes")
@RestController
public class AvaliacoesEventosController {

    private final AvaliacoesEventosService avaliacoesEventosService;


    public AvaliacoesEventosController(AvaliacoesEventosService avaliacoesEventosService) {
        this.avaliacoesEventosService = avaliacoesEventosService;
    }

    @PostMapping
    public ResponseEntity<?> salvarAvaliacao(@RequestBody AvaliacaoEventosDTO avaliacoesEventos){
        return ResponseEntity.ok().body(avaliacoesEventosService.save(avaliacoesEventos));
    }

    @PutMapping
    public ResponseEntity<?> atualizarAvaliacaoEventos(@RequestBody AvaliacaoEventosDTO avaliacoesEventos){
        return ResponseEntity.ok().body(avaliacoesEventosService.update(avaliacoesEventos));
    }

    @DeleteMapping("/{idAvalicao}")
    public ResponseEntity<?> deletarAvalicaoEventos(@PathVariable long idAvalicao){
        return ResponseEntity.ok().body(avaliacoesEventosService.deleteById(idAvalicao));
    }

    @GetMapping("/{idEvento}")
    public ResponseEntity<?> buscarAvaliacoesPorEvento(@PathVariable long idEvento) {
        return ResponseEntity.ok().body(avaliacoesEventosService.avaliacoesPorIdEvento(idEvento));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<?> buscarAvaliacoesPorCliente(@PathVariable UUID id) {
        return ResponseEntity.ok().body(avaliacoesEventosService.avaliacoesPorClientUserId(id));
    }


}
