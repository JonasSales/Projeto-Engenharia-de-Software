package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.OrganizacaoDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.OrganizacaoMapper;
import com.br.projetoyaskara.model.Endereco;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.repository.EnderecoRepository;
import com.br.projetoyaskara.repository.OrganizacaoRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.br.projetoyaskara.util.Utils.atualizarEndereco;

@Service
public class OrganizacaoService {

    private final OrganizacaoMapper organizacaoMapper;
    private final OrganizacaoRepository organizacaoRepository;
    private final EnderecoRepository enderecoRepository;

    public OrganizacaoService(OrganizacaoMapper organizacaoMapper, OrganizacaoRepository organizacaoRepository, EnderecoRepository enderecoRepository) {
        this.organizacaoMapper = organizacaoMapper;
        this.organizacaoRepository = organizacaoRepository;
        this.enderecoRepository = enderecoRepository;
    }

    private Organizacao findOrganizacaoOrThrow(UUID id) {
        return organizacaoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Organização não encontrado"));
    }

    public ResponseEntity<OrganizacaoDTO> registrarOrganizacao(OrganizacaoDTO organizacaoDTO) throws BadRequestException {
        try {
            Organizacao novaOrganizacao = new Organizacao();
            novaOrganizacao.setName(organizacaoDTO.getName());
            novaOrganizacao.setDescription(organizacaoDTO.getDescription());
            novaOrganizacao.setCnpj(organizacaoDTO.getCnpj());


            if (organizacaoDTO.getEndereco() != null) {
                Endereco novoEndereco = new Endereco();
                novoEndereco.setComplemento(organizacaoDTO.getEndereco().getComplemento());
                novoEndereco.setBairro(organizacaoDTO.getEndereco().getBairro());
                novoEndereco.setCidade(organizacaoDTO.getEndereco().getCidade());
                novoEndereco.setEstado(organizacaoDTO.getEndereco().getEstado());
                novoEndereco.setCep(organizacaoDTO.getEndereco().getCep());

                Endereco enderecoSalvo = enderecoRepository.save(novoEndereco);
                novaOrganizacao.setEndereco(enderecoSalvo);
            } else {
                throw new BadRequestException("Endereço da organização é obrigatório.");
            }

            Organizacao salva = organizacaoRepository.save(novaOrganizacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(organizacaoMapper.toDTO(salva));
        } catch (DataIntegrityViolationException e) {
            System.err.println("Erro de integridade ao registrar organização: " + e.getMessage());
            String errorMessage = "Violação de dados: CNPJ já cadastrado ou dados inválidos.";
            if (e.getRootCause() != null) {
                errorMessage += " Detalhes: " + e.getRootCause().getMessage();
            }
            throw new BadRequestException(errorMessage);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao registrar organização: " + e.getMessage());
            throw new RuntimeException("Erro interno ao registrar organização.");
        }
    }

    public ResponseEntity<OrganizacaoDTO> cadastrarEndereco(UUID idOrganizacao, Endereco endereco) throws BadRequestException {
        try {
            Organizacao organizacao = findOrganizacaoOrThrow(idOrganizacao);

            if (organizacao.getEndereco() != null) {
                throw new BadRequestException("Essa organização já possui um endereço cadastrado.");
            }

            Endereco novoEndereco = new Endereco();
            novoEndereco.setComplemento(endereco.getComplemento());
            novoEndereco.setBairro(endereco.getBairro());
            novoEndereco.setCidade(endereco.getCidade());
            novoEndereco.setEstado(endereco.getEstado());
            novoEndereco.setCep(endereco.getCep());

            Endereco enderecoSalvo = enderecoRepository.save(novoEndereco);
            organizacao.setEndereco(enderecoSalvo);
            Organizacao organizacaoAtualizada = organizacaoRepository.save(organizacao);

            return ResponseEntity.status(HttpStatus.OK).body(organizacaoMapper.toDTO(organizacaoAtualizada));
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e; // Relança as exceções específicas
        } catch (DataIntegrityViolationException e) {
            System.err.println("Erro de integridade ao cadastrar endereço: " + e.getMessage());
            throw new BadRequestException("Erro ao cadastrar endereço devido a violação de dados.");
        } catch (Exception e) {
            System.err.println("Erro inesperado ao cadastrar endereço para organização: " + e.getMessage());
            throw new RuntimeException("Erro interno ao cadastrar endereço.");
        }
    }

    public ResponseEntity<OrganizacaoDTO> updateOrganizacao(OrganizacaoDTO organizacaoDTO) throws BadRequestException {
        try {

            Organizacao organizacaoExistente = findOrganizacaoOrThrow(organizacaoDTO.getId());
            organizacaoExistente.setName(organizacaoDTO.getName());
            organizacaoExistente.setCnpj(organizacaoDTO.getCnpj());
            organizacaoExistente.setDescription(organizacaoDTO.getDescription());

            if (organizacaoDTO.getEndereco() != null) {
                Endereco enderecoExistente = organizacaoExistente.getEndereco();

                if (enderecoExistente == null) {
                    Endereco novoEndereco = new Endereco();
                    atualizarEndereco(novoEndereco, organizacaoDTO.getEndereco());
                    enderecoExistente = enderecoRepository.save(novoEndereco);
                    organizacaoExistente.setEndereco(enderecoExistente);
                } else {
                    atualizarEndereco(enderecoExistente, organizacaoDTO.getEndereco());
                    enderecoRepository.save(enderecoExistente);
                }
            } else {
                if (organizacaoExistente.getEndereco() == null) {
                    throw new BadRequestException("Endereço da organização é obrigatório e não foi fornecido na atualização.");
                }
            }


            Organizacao organizacaoSalva = organizacaoRepository.save(organizacaoExistente);
            return ResponseEntity.status(HttpStatus.OK).body(organizacaoMapper.toDTO(organizacaoSalva));
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            System.err.println("Erro de integridade ao atualizar organização: " + e.getMessage());
            throw new BadRequestException("Erro ao atualizar organização devido a violação de dados.");
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar organização: " + e.getMessage());
            throw new RuntimeException("Erro interno ao atualizar organização.");
        }
    }

    public ResponseEntity<Void> deletarOrganizacao(UUID id) {
        try {
            Organizacao organizacao = findOrganizacaoOrThrow(id);
            organizacaoRepository.delete(organizacao);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao deletar organização: " + e.getMessage());
            throw new RuntimeException("Erro interno ao deletar organização.");
        }
    }

    public ResponseEntity<List<OrganizacaoDTO>> getAllOrganizacoes() {
        try {
            List<Organizacao> organizacoes = organizacaoRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(organizacaoMapper.toDTO(organizacoes));
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar todas as organizações: " + e.getMessage());
            throw new RuntimeException("Erro interno ao buscar organizações.");
        }
    }

    public ResponseEntity<OrganizacaoDTO> getOrganizacaoById(UUID id) {
        try {
            Organizacao organizacao = findOrganizacaoOrThrow(id);
            return ResponseEntity.status(HttpStatus.OK).body(organizacaoMapper.toDTO(organizacao));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar organização por ID: " + e.getMessage());
            throw new RuntimeException("Erro interno ao buscar organização.");
        }
    }

    public ResponseEntity<List<OrganizacaoDTO>> getOrganizacaoByName(String name) {
        try {
            List<Organizacao> organizacoes = organizacaoRepository.findAllByNameContaining(name);
            return ResponseEntity.ok(organizacaoMapper.toDTO(organizacoes));
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar organizações por nome: " + e.getMessage());
            throw new RuntimeException("Erro interno ao buscar organizações por nome.");
        }
    }

}