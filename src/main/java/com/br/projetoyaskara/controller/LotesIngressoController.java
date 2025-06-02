package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.FaixaDePreco;
import com.br.projetoyaskara.dto.LotesIngressoDTO;
import com.br.projetoyaskara.model.LotesIngresso;
import com.br.projetoyaskara.service.LotesIngressosService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ingressos")
public class LotesIngressoController {

    final private LotesIngressosService lotesIngressosService;


    public LotesIngressoController(LotesIngressosService lotesIngressosService) {
        this.lotesIngressosService = lotesIngressosService;
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody LotesIngresso lotesIngresso) {
        return ResponseEntity.ok(lotesIngressosService.cadastrarIngresso(lotesIngresso));
    }

    @PutMapping()
    public ResponseEntity<?> update(@RequestBody LotesIngressoDTO lotesIngresso) {
        return ResponseEntity.ok(lotesIngressosService.atualizarIngresso(lotesIngresso));
    }

    @DeleteMapping()
    public ResponseEntity<?> delete(@RequestBody LotesIngressoDTO lotesIngresso) {
        return ResponseEntity.ok(lotesIngressosService.deletarIngresso(lotesIngresso));
    }

    @GetMapping()
    public ResponseEntity<?> buscarIngressos() {
        return ResponseEntity.ok(lotesIngressosService.listarIngressos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarIngressoId(@PathVariable Long id) {
        return ResponseEntity.ok(lotesIngressosService.buscarIngressoPorId(id));
    }

    @GetMapping("/evento/{id}")
    public ResponseEntity<?> buscarIngressoPorIdEvento(@PathVariable Long id) {
        return ResponseEntity.ok(lotesIngressosService.buscarIngressosPorEventoId(id));
    }

    @GetMapping("/preco")
    public ResponseEntity<?> buscarIngressoPorPreco(@RequestBody FaixaDePreco faixaDePreco) {
        return ResponseEntity.ok(lotesIngressosService
                .buscarIngressosPorFaixaDePreco(faixaDePreco.menorPreco(), faixaDePreco.maiorPreco()));
    }

}
