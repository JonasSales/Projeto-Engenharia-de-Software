package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.FaixaDePreco;
import com.br.projetoyaskara.dto.LotesIngressoDTO;
import com.br.projetoyaskara.service.LotesIngressosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ingressos")
public class LotesIngressoController {

    final private LotesIngressosService lotesIngressosService;

    public LotesIngressoController(LotesIngressosService lotesIngressosService) {
        this.lotesIngressosService = lotesIngressosService;
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody LotesIngressoDTO lotesIngressoDTO) {
        return lotesIngressosService.cadastrarIngresso(lotesIngressoDTO);
    }

    @PutMapping()
    public ResponseEntity<?> update(@Valid @RequestBody LotesIngressoDTO lotesIngressoDTO) {
        return lotesIngressosService.atualizarIngresso(lotesIngressoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return lotesIngressosService.deletarIngresso(id);
    }

    @GetMapping()
    public ResponseEntity<?> buscarIngressos() {
        return lotesIngressosService.listarIngressos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarIngressoId(@PathVariable Long id) {
        return lotesIngressosService.buscarIngressoPorId(id);
    }

    @GetMapping("/evento/{id}")
    public ResponseEntity<?> buscarIngressoPorIdEvento(@PathVariable Long id) {
        return lotesIngressosService.buscarIngressosPorEventoId(id);
    }

    @GetMapping("/preco")
    public ResponseEntity<?> buscarIngressoPorPreco(@RequestBody FaixaDePreco faixaDePreco) {
        return lotesIngressosService
                .buscarIngressosPorFaixaDePreco(faixaDePreco.menorPreco(), faixaDePreco.maiorPreco());
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> aumentarVenda(@PathVariable long id){
        return lotesIngressosService.aumentarVendaEm1(id);
    }

}