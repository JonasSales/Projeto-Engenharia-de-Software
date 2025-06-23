package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.AvaliacaoEventosDTO;
import com.br.projetoyaskara.service.AvaliacoesEventosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/avaliacoes")
@RestController
public class AvaliacoesEventosController {

    private final AvaliacoesEventosService avaliacoesEventosService;

    public AvaliacoesEventosController(AvaliacoesEventosService avaliacoesEventosService) {
        this.avaliacoesEventosService = avaliacoesEventosService;
    }

    @PostMapping
    public ResponseEntity<AvaliacaoEventosDTO> salvarAvaliacao(Authentication authentication,
                                             @Valid @RequestBody AvaliacaoEventosDTO avaliacoesEventos){
        return avaliacoesEventosService.save(authentication,avaliacoesEventos);
    }

    @PutMapping
    public ResponseEntity<AvaliacaoEventosDTO> atualizarAvaliacaoEventos(Authentication authentication ,
                                                       @Valid @RequestBody AvaliacaoEventosDTO avaliacoesEventos){
        return avaliacoesEventosService.update(authentication,avaliacoesEventos);
    }

    @DeleteMapping("/{idAvalicao}")
    public ResponseEntity<Void> deletarAvalicaoEventos(Authentication authentication,
                                                    @PathVariable long idAvalicao){
        return avaliacoesEventosService.deleteById(authentication,idAvalicao);
    }

    @GetMapping("/{idEvento}")
    public ResponseEntity<List<AvaliacaoEventosDTO>> buscarAvaliacoesPorEvento(@PathVariable long idEvento) {
        return avaliacoesEventosService.avaliacoesPorIdEvento(idEvento);
    }


    @GetMapping()
    public ResponseEntity<List<AvaliacaoEventosDTO>> buscarAvaliacoesPorCliente(Authentication authentication) {
        return avaliacoesEventosService.avaliacoesPorUser(authentication);
    }
}
