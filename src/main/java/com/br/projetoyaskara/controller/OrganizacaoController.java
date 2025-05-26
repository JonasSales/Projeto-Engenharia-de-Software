package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.service.OrganizacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/organizacao")
public class OrganizacaoController {

    private final OrganizacaoService organizacaoService;

    public OrganizacaoController(OrganizacaoService organizacaoService) {
        this.organizacaoService = organizacaoService;
    }

    @PostMapping("/create")
    ResponseEntity<?> registerOrganizacao(@RequestBody Organizacao organizacao) {
        return organizacaoService.registerOrganization(organizacao);
    }

    @GetMapping("/get")
    ResponseEntity<?> getAllOrganizacao() {
        return organizacaoService.getAllOrganizacoes();
    }

    @GetMapping("/get/{name}")
    ResponseEntity<?> getOrganizacaoByName(@PathVariable String name) {
        return organizacaoService.getOrganizacaoByName(name);
    }

}
