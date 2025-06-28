package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.EnderecoDTO;
import com.br.projetoyaskara.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    // ===================== CLIENTE =====================

    @PostMapping("/cliente")
    public ResponseEntity<EnderecoDTO> cadastrarEnderecoCliente(Authentication authentication,
                                                                @Valid @RequestBody EnderecoDTO enderecoDTO) {
        return enderecoService.cadastrarEnderecoClient(authentication, enderecoDTO);
    }

    @PutMapping("/cliente")
    public ResponseEntity<EnderecoDTO> atualizarEnderecoCliente(Authentication authentication,
                                                                @Valid @RequestBody EnderecoDTO enderecoDTO) {
        return enderecoService.atualizarEnderecoClient(authentication, enderecoDTO);
    }

    @GetMapping("/cliente")
    public ResponseEntity<EnderecoDTO> buscarEnderecoCliente(Authentication authentication) {
        return enderecoService.buscarEnderecoCLient(authentication);
    }

    @DeleteMapping("/cliente")
    public ResponseEntity<String> deletarEnderecoCliente(Authentication authentication) {
        return enderecoService.deletarEnderecoClient(authentication);
    }

    // ===================== EVENTO =====================

    @PostMapping("/evento/{idEvento}")
    public ResponseEntity<EnderecoDTO> cadastrarEnderecoEvento(Authentication authentication,
                                                               @Valid @RequestBody EnderecoDTO enderecoDTO,
                                                               @PathVariable("idEvento") long idEvento) {
        return enderecoService.cadastrarEnderecoEvento(authentication, enderecoDTO, idEvento);
    }

    @PutMapping("/evento/{idEvento}")
    public ResponseEntity<EnderecoDTO> atualizarEnderecoEvento(Authentication authentication,
                                                               @PathVariable long idEvento,
                                                               @Valid @RequestBody EnderecoDTO enderecoDTO) {
        return enderecoService.atualizarEnderecoEvento(authentication, idEvento, enderecoDTO);
    }

    @GetMapping("/evento/{idEvento}")
    public ResponseEntity<EnderecoDTO> buscarEnderecoEvento(Authentication authentication,
                                                            @PathVariable long idEvento) {
        return enderecoService.buscarEnderecoEvento(authentication, idEvento);
    }

    @DeleteMapping("/evento/{idEvento}")
    public ResponseEntity<String> deletarEnderecoEvento(Authentication authentication,
                                                        @PathVariable long idEvento) {
        return enderecoService.deletarEnderecoEvento(authentication, idEvento);
    }

    // ===================== ORGANIZAÇÃO =====================

    @PostMapping("/organizacao/{idOrganizacao}")
    public ResponseEntity<EnderecoDTO> cadastrarEnderecoOrganizacao(Authentication authentication,
                                                                    @Valid @RequestBody EnderecoDTO enderecoDTO,
                                                                    @PathVariable("idOrganizacao") UUID idOrganizacao) {

        return enderecoService.cadastrarEnderecoOrganizacao(authentication, idOrganizacao, enderecoDTO);
    }

    @PutMapping("/organizacao/{idOrganizacao}")
    public ResponseEntity<EnderecoDTO> atualizarEnderecoOrganizacao(Authentication authentication,
                                                                    @PathVariable UUID idOrganizacao,
                                                                    @Valid @RequestBody EnderecoDTO enderecoDTO) {
        return enderecoService.atualizarEnderecoOrganizacao(authentication, idOrganizacao, enderecoDTO);
    }

    @GetMapping("/organizacao/{idOrganizacao}")
    public ResponseEntity<EnderecoDTO> buscarEnderecoOrganizacao(Authentication authentication,
                                                                 @PathVariable UUID idOrganizacao) {
        return enderecoService.buscarEnderecoOrganizacao(authentication, idOrganizacao);
    }

    @DeleteMapping("/organizacao/{idOrganizacao}")
    public ResponseEntity<String> deletarEnderecoOrganizacao(Authentication authentication,
                                                             @PathVariable UUID idOrganizacao) {
        return enderecoService.deletarEnderecoOrganizacao(authentication, idOrganizacao);
    }
}
