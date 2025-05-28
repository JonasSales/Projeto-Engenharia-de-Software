package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.OrganizacaoDTO;
import com.br.projetoyaskara.model.Endereco;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.repository.EnderecoRepository;
import com.br.projetoyaskara.repository.OrganizacaoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.br.projetoyaskara.util.Utils.atualizarEndereco;

@Service
public class OrganizacaoService {

    private final OrganizacaoRepository organizacaoRepository;
    private final EnderecoRepository enderecoRepository;

    public OrganizacaoService(OrganizacaoRepository organizacaoRepository, EnderecoRepository enderecoRepository) {
        this.organizacaoRepository = organizacaoRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public ResponseEntity<?> registrarOrganizacao(Organizacao organizacao) {
        try {
            if (organizacao.getEndereco() != null) {
                Endereco enderecoSalvo = enderecoRepository.save(organizacao.getEndereco());
                organizacao.setEndereco(enderecoSalvo);
            }
            Organizacao salva = organizacaoRepository.save(organizacao);
            return ResponseEntity.ok(toDTO(salva));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Dados inválidos ou faltando: " + Objects.requireNonNull(e.getRootCause()).getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado ao criar a organização: " + e.getMessage());
        }
    }

    public ResponseEntity<?> cadastrarEndereco(UUID idOrganizacao, Endereco endereco) {
        try {
            Organizacao organizacao = organizacaoRepository.findOrganizacaoById(idOrganizacao);

            if (organizacao == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Organização não encontrada no banco de dados. Verifique o ID.");
            }

            if (organizacao.getEndereco() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Essa organização já possui um endereço cadastrado.");
            }

            Endereco enderecoSalvo = enderecoRepository.save(endereco);
            organizacao.setEndereco(enderecoSalvo);
            organizacaoRepository.save(organizacao);

            return ResponseEntity.ok(toDTO(organizacao));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar organização " + e.getMessage());
        }

    }

    public ResponseEntity<?> updateOrganizacao(Organizacao organizacao) {
        Organizacao updatedOrganizacao = organizacaoRepository.findOrganizacaoById(organizacao.getId());

        if (updatedOrganizacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Organização não encontrada no banco de dados. Verifique o ID.");
        }

        if (updatedOrganizacao.getEndereco() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Organização não possui endereço cadastrado.");
        }

        Endereco enderecoSalvo = enderecoRepository.findEnderecoById(updatedOrganizacao.getEndereco().getId());

        if (enderecoSalvo == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Endereço associado à organização não encontrado.");
        }

        atualizarEndereco(enderecoSalvo, organizacao.getEndereco());
        enderecoRepository.save(enderecoSalvo);

        updatedOrganizacao.setName(organizacao.getName());
        updatedOrganizacao.setCnpj(organizacao.getCnpj());
        updatedOrganizacao.setEndereco(enderecoSalvo);

        organizacaoRepository.save(updatedOrganizacao);
        return ResponseEntity.ok(toDTO(updatedOrganizacao));
    }

    public ResponseEntity<?> deletarOrganizacao(UUID id) {

        if (organizacaoRepository.findOrganizacaoById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe organização com este ID");
        }

        organizacaoRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("A organização do ID " + id + " foi deletada com sucesso.");
    }

    public ResponseEntity<?> getAllOrganizacoes() {
        List<Organizacao> organizacoes = organizacaoRepository.findAll();
        List<OrganizacaoDTO> dtos = organizacoes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> getOrganizacaoById(UUID id) {
        Organizacao organizacao = organizacaoRepository.findOrganizacaoById(id);
        if (organizacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Organização não encontrada com o ID informado.");
        }
        return ResponseEntity.ok(toDTO(organizacao));
    }

    public ResponseEntity<?> getOrganizacaoByName(String name) {
        List<Organizacao> organizacoes = organizacaoRepository.findAllByNameContaining(name);
        List<OrganizacaoDTO> dtos = organizacoes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }


    private OrganizacaoDTO toDTO(Organizacao organizacao) {
        return new OrganizacaoDTO(organizacao);
    }
}
