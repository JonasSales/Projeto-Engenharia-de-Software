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

    @PostMapping()
    ResponseEntity<?> registerOrganizacao(@RequestBody Organizacao organizacao) {
        return ResponseEntity.ok().body(organizacaoService.registrarOrganizacao(organizacao));
    }

    @PostMapping("/endereco/{id}")
    ResponseEntity<?> cadastraEndereco(@PathVariable UUID id, @RequestBody Endereco endereco) {
        return ResponseEntity.ok().body(organizacaoService.cadastrarEndereco(id, endereco));
    }

    @GetMapping()
    ResponseEntity<?> getAllOrganizacao() {
        return ResponseEntity.ok().body(organizacaoService.getAllOrganizacoes());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getOrganizacaoByName(@PathVariable String name) {
        return ResponseEntity.ok().body(organizacaoService.getOrganizacaoByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrganizacaoById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(organizacaoService.getOrganizacaoById(id));
    }


    @PutMapping()
    ResponseEntity<?> updateOrganizacao(@RequestBody Organizacao organizacao) {
        return ResponseEntity.ok().body(organizacaoService.updateOrganizacao(organizacao));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteOrganizacao(@PathVariable UUID id) {
        return ResponseEntity.ok().body(organizacaoService.deletarOrganizacao(id));
    }


}
