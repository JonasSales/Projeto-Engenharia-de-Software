package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.service.LotesIngressosService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ingressos")
public class LotesIngressoController {

    final private LotesIngressosService lotesIngressosService;


    public LotesIngressoController(LotesIngressosService lotesIngressosService) {
        this.lotesIngressosService = lotesIngressosService;
    }

    @GetMapping("/get")
    public ResponseEntity<?> buscarIngressos() {
        return ResponseEntity.ok(lotesIngressosService.listarIngressos());
    }
}
