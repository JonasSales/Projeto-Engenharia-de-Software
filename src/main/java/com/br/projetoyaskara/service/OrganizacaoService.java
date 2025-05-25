package com.br.projetoyaskara.service;

import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.repository.OrganizacaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrganizacaoService {

    private final OrganizacaoRepository organizacaoRepository;

    public OrganizacaoService(OrganizacaoRepository organizacaoRepository) {
        this.organizacaoRepository = organizacaoRepository;
    }

    public ResponseEntity<?> registerOrganization(Organizacao organizacao) {
        return ResponseEntity.ok(organizacaoRepository.save(organizacao));
    }

    public ResponseEntity<?> getAllOrganizacoes() {
        return ResponseEntity.ok(organizacaoRepository.findAll());
    }

    public ResponseEntity<?> getOrganizacaoByName(String name) {
        return ResponseEntity.ok(organizacaoRepository.findAllByNameContaining(name));
    }
}
