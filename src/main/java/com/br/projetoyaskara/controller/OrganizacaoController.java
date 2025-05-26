package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.model.Endereco;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.service.OrganizacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @PostMapping("/create/endereco/{id}")
    ResponseEntity<?> cadastraEndereco(@PathVariable UUID id, @RequestBody Endereco endereco) {
        return organizacaoService.cadastrarEndereco(id, endereco);
    }

    @GetMapping("/get")
    ResponseEntity<?> getAllOrganizacao() {
        return organizacaoService.getAllOrganizacoes();
    }

    @GetMapping("/get/name/{name}")
    ResponseEntity<?> getOrganizacaoByName(@PathVariable String name) {
        return organizacaoService.getOrganizacaoByName(name);
    }

    @GetMapping("/get/id/{id}")
    ResponseEntity<?> getOrganizacaoById(@PathVariable UUID id) {
        return organizacaoService.getOrganizacaoById(id);
    }

    @PostMapping("/update")
    ResponseEntity<?> updateOrganizacao(@RequestBody Organizacao organizacao) {
        return organizacaoService.updateOrganizacao(organizacao);
    }


}
