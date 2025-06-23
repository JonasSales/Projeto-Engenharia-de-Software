package com.br.projetoyaskara.controller;


import com.br.projetoyaskara.dto.OrganizacaoDTO;
import com.br.projetoyaskara.service.OrganizacaoService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/organizacao")
public class OrganizacaoController {

    private final OrganizacaoService organizacaoService;

    public OrganizacaoController(OrganizacaoService organizacaoService) {
        this.organizacaoService = organizacaoService;
    }

    @PreAuthorize("hasAnyAuthority('organization::create')")
    @PostMapping()
    ResponseEntity<?> registerOrganizacao(Authentication authentication, @Valid @RequestBody OrganizacaoDTO organizacaoDTO) throws BadRequestException {
        return organizacaoService.registrarOrganizacao(authentication,organizacaoDTO);
    }

    @PreAuthorize("hasAnyAuthority('organization::put')")
    @PutMapping()
    ResponseEntity<?> updateOrganizacao(Authentication authentication, @Valid @RequestBody OrganizacaoDTO organizacaoDTO) throws BadRequestException {
        return organizacaoService.updateOrganizacao(authentication, organizacaoDTO);
    }

    @PreAuthorize("hasAnyAuthority('organization::delete')")
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteOrganizacao(Authentication authentication,@PathVariable UUID id) {
        return organizacaoService.deletarOrganizacao(authentication, id);
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




}
