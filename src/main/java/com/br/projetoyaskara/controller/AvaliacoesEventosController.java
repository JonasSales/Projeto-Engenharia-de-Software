package com.br.projetoyaskara.controller;


import com.br.projetoyaskara.dto.request.AvaliacaoRequestDTO;
import com.br.projetoyaskara.dto.response.AvaliacaoResponseDTO;
import com.br.projetoyaskara.service.AvaliacoesEventosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequestMapping("/avaliacoes")
@RestController
public class AvaliacoesEventosController {

    private final AvaliacoesEventosService avaliacoesEventosService;

    public AvaliacoesEventosController(AvaliacoesEventosService avaliacoesEventosService) {
        this.avaliacoesEventosService = avaliacoesEventosService;
    }

    @PostMapping
    public ResponseEntity<AvaliacaoResponseDTO> salvarAvaliacao(Authentication authentication,
                                                                @Valid @RequestBody AvaliacaoRequestDTO avaliacoesEventos){
        return avaliacoesEventosService.save(authentication,avaliacoesEventos);
    }

    @PutMapping
    public ResponseEntity<AvaliacaoResponseDTO> atualizarAvaliacaoEventos(Authentication authentication ,
                                                       @Valid @RequestBody AvaliacaoRequestDTO avaliacoesEventos){
        return avaliacoesEventosService.update(authentication,avaliacoesEventos);
    }

    @DeleteMapping("/{idAvalicao}")
    public ResponseEntity<Void> deletarAvalicaoEventos(Authentication authentication,
                                                    @PathVariable UUID idAvalicao){
        return avaliacoesEventosService.deleteById(authentication,idAvalicao);
    }

    @GetMapping("/{idEvento}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> buscarAvaliacoesPorEvento(@PathVariable UUID idEvento) {
        return avaliacoesEventosService.avaliacoesPorIdEvento(idEvento);
    }


    @GetMapping()
    public ResponseEntity<List<AvaliacaoResponseDTO>> buscarAvaliacoesPorCliente(Authentication authentication) {
        return avaliacoesEventosService.avaliacoesPorUser(authentication);
    }
}
