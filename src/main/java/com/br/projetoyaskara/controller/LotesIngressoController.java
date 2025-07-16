package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.FaixaDePreco;
import com.br.projetoyaskara.dto.request.LoteIngressoCreateRequestDTO;
import com.br.projetoyaskara.dto.request.LoteIngressoUpdateRequestDTO;
import com.br.projetoyaskara.dto.response.LoteIngressoResponseDTO;
import com.br.projetoyaskara.service.LotesIngressosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ingressos")
public class LotesIngressoController {

    final private LotesIngressosService lotesIngressosService;

    public LotesIngressoController(LotesIngressosService lotesIngressosService) {
        this.lotesIngressosService = lotesIngressosService;
    }

    @PreAuthorize("hasAnyAuthority('organization::create')")
    @PostMapping()
    public ResponseEntity<LoteIngressoResponseDTO> create(Authentication authentication,
                                                          @Valid @RequestBody LoteIngressoCreateRequestDTO lotesIngressoDTO) {
        return lotesIngressosService.cadastrarIngresso(authentication, lotesIngressoDTO);
    }

    @PreAuthorize("hasAnyAuthority('organization::put')")
    @PutMapping()
    public ResponseEntity<LoteIngressoResponseDTO> update(Authentication authentication,
                                                   @Valid @RequestBody LoteIngressoUpdateRequestDTO lotesIngressoDTO) {
        return lotesIngressosService.atualizarIngresso(authentication, lotesIngressoDTO);
    }

    @PreAuthorize("hasAnyAuthority('organization::delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable UUID id) {
        return lotesIngressosService.deletarIngresso(authentication, id);
    }

    @GetMapping()
    public ResponseEntity<List<LoteIngressoResponseDTO>> buscarIngressos() {
        return lotesIngressosService.listarIngressos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteIngressoResponseDTO> buscarIngressoId(@PathVariable UUID id) {
        return lotesIngressosService.buscarIngressoPorId(id);
    }

    @GetMapping("/evento/{id}")
    public ResponseEntity<List<LoteIngressoResponseDTO>> buscarIngressoPorIdEvento(@PathVariable UUID id) {
        return lotesIngressosService.buscarIngressosPorEventoId(id);
    }

    @GetMapping("/preco")
    public ResponseEntity<List<LoteIngressoResponseDTO>> buscarIngressoPorPreco(@RequestBody FaixaDePreco faixaDePreco) {
        return lotesIngressosService
                .buscarIngressosPorFaixaDePreco(faixaDePreco.menorPreco(), faixaDePreco.maiorPreco());
    }

}