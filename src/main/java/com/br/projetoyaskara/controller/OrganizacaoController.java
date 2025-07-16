package com.br.projetoyaskara.controller;


import com.br.projetoyaskara.dto.request.OrganizacaoCreateRequestDTO;
import com.br.projetoyaskara.dto.request.OrganizacaoUpdateRequestDTO;
import com.br.projetoyaskara.dto.response.OrganizacaoResponseDTO;
import com.br.projetoyaskara.service.OrganizacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    ResponseEntity<OrganizacaoResponseDTO> registerOrganizacao(
            Authentication authentication, @Valid @RequestBody OrganizacaoCreateRequestDTO organizacaoDTO) {
        return organizacaoService.registrarOrganizacao(authentication,organizacaoDTO);
    }

    @PreAuthorize("hasAnyAuthority('organization::put')")
    @PutMapping()
    ResponseEntity<OrganizacaoResponseDTO> updateOrganizacao(
            Authentication authentication, @Valid @RequestBody OrganizacaoUpdateRequestDTO organizacaoDTO) {
        return organizacaoService.updateOrganizacao(authentication, organizacaoDTO);
    }

    @PreAuthorize("hasAnyAuthority('organization::delete')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteOrganizacao(Authentication authentication,@PathVariable UUID id) {
        return organizacaoService.deletarOrganizacao(authentication, id);
    }

    @GetMapping()
    ResponseEntity<List<OrganizacaoResponseDTO>> getAllOrganizacao() {
        return organizacaoService.getAllOrganizacoes();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<OrganizacaoResponseDTO>> getOrganizacaoByName(@PathVariable String name) {
        return organizacaoService.getOrganizacaoByName(name);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrganizacaoResponseDTO> getOrganizacaoById(@PathVariable UUID id) {
        return organizacaoService.getOrganizacaoById(id);
    }

    @PreAuthorize("hasAnyAuthority('organization::read')")
    @GetMapping("/organizations")
    public ResponseEntity<List<OrganizacaoResponseDTO>> findAllOrganizacaoByProprietario(Authentication authentication) {
        return organizacaoService.getOrganizacoesByIdProprietario(authentication);
    }

}
