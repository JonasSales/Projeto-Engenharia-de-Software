package com.br.projetoyaskara.controller;


import com.br.projetoyaskara.dto.OrganizacaoDTO;
import com.br.projetoyaskara.model.Endereco;
import com.br.projetoyaskara.service.OrganizacaoService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/organizacao")
public class OrganizacaoController {

    private final OrganizacaoService organizacaoService;

    public OrganizacaoController(OrganizacaoService organizacaoService) {
        this.organizacaoService = organizacaoService;
    }

    @PostMapping()
    ResponseEntity<?> registerOrganizacao(@Valid @RequestBody OrganizacaoDTO organizacaoDTO) throws BadRequestException {
        return organizacaoService.registrarOrganizacao(organizacaoDTO);
    }

    @PostMapping("/endereco/{id}")
    ResponseEntity<?> cadastraEndereco(@PathVariable UUID id, @Valid @RequestBody Endereco endereco) throws BadRequestException {

        return organizacaoService.cadastrarEndereco(id, endereco);
    }

    @GetMapping()
    ResponseEntity<?> getAllOrganizacao() {
        return organizacaoService.getAllOrganizacoes();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getOrganizacaoByName(@PathVariable String name) {
        return organizacaoService.getOrganizacaoByName(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrganizacaoById(@PathVariable UUID id) {
        return organizacaoService.getOrganizacaoById(id);
    }

    @PutMapping()
    ResponseEntity<?> updateOrganizacao(@Valid @RequestBody OrganizacaoDTO organizacaoDTO) throws BadRequestException {
        return organizacaoService.updateOrganizacao(organizacaoDTO);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteOrganizacao(@PathVariable UUID id) {
        return organizacaoService.deletarOrganizacao(id);
    }
}