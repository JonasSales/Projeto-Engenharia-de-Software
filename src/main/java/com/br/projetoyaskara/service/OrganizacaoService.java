package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.OrganizacaoDTO;
import com.br.projetoyaskara.model.Endereco;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.repository.EnderecoRepository;
import com.br.projetoyaskara.repository.OrganizacaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizacaoService {

    private final OrganizacaoRepository organizacaoRepository;
    private final EnderecoRepository enderecoRepository;

    public OrganizacaoService(OrganizacaoRepository organizacaoRepository, EnderecoRepository enderecoRepository) {
        this.organizacaoRepository = organizacaoRepository;
        this.enderecoRepository = enderecoRepository;
    }


    public ResponseEntity<?> registerOrganization(Organizacao organizacao) {
        if (organizacao.getEndereco() != null) {
            Endereco enderecoSalvo = enderecoRepository.save(organizacao.getEndereco());
            organizacao.setEndereco(enderecoSalvo);
        }
        return ResponseEntity.ok(organizacaoRepository.save(organizacao));
    }

    public ResponseEntity<?> getAllOrganizacoes() {
        List<Organizacao> eventos = organizacaoRepository.findAll();
        List<OrganizacaoDTO> dtos = eventos.stream()
                .map(OrganizacaoDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> getOrganizacaoById(UUID id) {
        return ResponseEntity.ok(organizacaoRepository.findOrganizacaoById(id));
    }
    public ResponseEntity<?> getOrganizacaoByName(String name) {
        List<Organizacao> eventos = organizacaoRepository.findAllByNameContaining(name);
        List<OrganizacaoDTO> dtos = eventos.stream()
                .map(OrganizacaoDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> cadastrarEndereco(UUID idOrganizacao, Endereco endereco) {
        Organizacao organizacao = organizacaoRepository.findOrganizacaoById(idOrganizacao);

        if (organizacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Essa organização não existe no banco de dados" +
                    ", verifique se o ID está correto");
        }

        Endereco enderecoSalvo = enderecoRepository.save(endereco);
        organizacao.setEndereco(enderecoSalvo);
        return ResponseEntity.ok(organizacaoRepository.save(organizacao));
    }
}
