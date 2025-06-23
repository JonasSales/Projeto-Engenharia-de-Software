package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.OrganizacaoDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.OrganizacaoMapper;
import com.br.projetoyaskara.model.clientuser.ClientUser;
import com.br.projetoyaskara.model.Endereco;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.repository.EnderecoRepository;
import com.br.projetoyaskara.repository.OrganizacaoRepository;
import com.br.projetoyaskara.repository.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.br.projetoyaskara.util.Utils.atualizarEndereco;

@Service
public class OrganizacaoService {

    private final OrganizacaoMapper organizacaoMapper;
    private final OrganizacaoRepository organizacaoRepository;
    private final EnderecoRepository enderecoRepository;
    private final UserRepository userRepository;

    public OrganizacaoService(OrganizacaoMapper organizacaoMapper, OrganizacaoRepository organizacaoRepository, EnderecoRepository enderecoRepository, UserRepository userRepository) {
        this.organizacaoMapper = organizacaoMapper;
        this.organizacaoRepository = organizacaoRepository;
        this.enderecoRepository = enderecoRepository;
        this.userRepository = userRepository;
    }

    private ClientUser findClientOrThrow(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    private Organizacao findOrganizacaoOrThrow(UUID id) {
        return organizacaoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Organização não encontrado"));
    }

    public ResponseEntity<OrganizacaoDTO> registrarOrganizacao(Authentication authentication, OrganizacaoDTO organizacaoDTO) throws BadRequestException {
        try {
            UUID idUsuarioAutenticado = userRepository.findIdByEmail(authentication.getName());

            if (!idUsuarioAutenticado.equals(organizacaoDTO.getIdProprietario())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ClientUser clientUser = findClientOrThrow(idUsuarioAutenticado);

            Organizacao novaOrganizacao = new Organizacao();
            novaOrganizacao.setProprietario(clientUser);
            novaOrganizacao.setName(organizacaoDTO.getName());
            novaOrganizacao.setDescription(organizacaoDTO.getDescription());
            novaOrganizacao.setCnpj(organizacaoDTO.getCnpj());

            if (organizacaoDTO.getEndereco() == null) {
                throw new BadRequestException("Endereço da organização é obrigatório.");
            }

            Endereco novoEndereco = new Endereco();
            atualizarEndereco(novoEndereco, organizacaoDTO.getEndereco());
            Endereco enderecoSalvo = enderecoRepository.save(novoEndereco);
            novaOrganizacao.setEndereco(enderecoSalvo);

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
            throw new RuntimeException("Erro interno ao registrar organização.");
        }
    }


    public ResponseEntity<OrganizacaoDTO> updateOrganizacao(Authentication authentication, OrganizacaoDTO organizacaoDTO) throws BadRequestException {
        try {

            UUID idProprietario = userRepository.findIdByEmail(authentication.getName());
            Organizacao organizacaoExistente = findOrganizacaoOrThrow(organizacaoDTO.getId());

            if (!idProprietario.equals(organizacaoExistente.getProprietario().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

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
            return ResponseEntity.ok(organizacaoMapper.toDTO(organizacaoSalva));

        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Erro ao atualizar organização: violação de integridade dos dados.");
        } catch (Exception e) {
            throw new RuntimeException("Erro interno ao atualizar organização.");
        }
    }


    public ResponseEntity<Void> deletarOrganizacao(Authentication authentication, UUID organizacaoId) {
        try {
            UUID idProprietario = userRepository.findIdByEmail(authentication.getName());
            Organizacao organizacao = findOrganizacaoOrThrow(organizacaoId);

            if (!idProprietario.equals(organizacao.getProprietario().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            organizacaoRepository.delete(organizacao);
            return ResponseEntity.noContent().build();

        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro interno ao deletar organização.");
        }
    }


    public ResponseEntity<List<OrganizacaoDTO>> getAllOrganizacoes() {
        try {
            List<Organizacao> organizacoes = organizacaoRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(organizacaoMapper.toDTO(organizacoes));
        } catch (Exception e) {
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
            throw new RuntimeException("Erro interno ao buscar organização.");
        }
    }

    public ResponseEntity<List<OrganizacaoDTO>> getOrganizacaoByName(String name) {
        try {
            List<Organizacao> organizacoes = organizacaoRepository.findAllByNameContaining(name);
            return ResponseEntity.ok(organizacaoMapper.toDTO(organizacoes));
        } catch (Exception e) {
            throw new RuntimeException("Erro interno ao buscar organizações por nome.");
        }
    }

}