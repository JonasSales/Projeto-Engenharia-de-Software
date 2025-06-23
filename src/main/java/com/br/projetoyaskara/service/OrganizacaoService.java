package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.OrganizacaoDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.OrganizacaoMapper;
import com.br.projetoyaskara.model.clientuser.ClientUser;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.repository.OrganizacaoRepository;
import com.br.projetoyaskara.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizacaoService {

    private final OrganizacaoMapper organizacaoMapper;
    private final OrganizacaoRepository organizacaoRepository;
    private final UserRepository userRepository;

    public OrganizacaoService(OrganizacaoMapper organizacaoMapper,
                              OrganizacaoRepository organizacaoRepository,
                              UserRepository userRepository) {
        this.organizacaoMapper = organizacaoMapper;
        this.organizacaoRepository = organizacaoRepository;
        this.userRepository = userRepository;
    }

    private ClientUser findClientOrThrow(UUID uuid) {
        return userRepository
                .findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    private Organizacao findOrganizacaoOrThrow(UUID id) {
        return organizacaoRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Organização não encontrado"));
    }

    public ResponseEntity<OrganizacaoDTO> registrarOrganizacao(
            Authentication authentication, OrganizacaoDTO organizacaoDTO) {

            UUID idUsuarioAutenticado = userRepository.findIdByEmail(authentication.getName());

            ClientUser clientUser = findClientOrThrow(idUsuarioAutenticado);

            Organizacao novaOrganizacao = new Organizacao();

            novaOrganizacao.setProprietario(clientUser);
            novaOrganizacao.setName(organizacaoDTO.getName());
            novaOrganizacao.setDescription(organizacaoDTO.getDescription());
            novaOrganizacao.setCnpj(organizacaoDTO.getCnpj());

            Organizacao salva = organizacaoRepository.save(novaOrganizacao);

            return ResponseEntity.status(HttpStatus.CREATED).body(organizacaoMapper.toDTO(salva));
    }


    public ResponseEntity<OrganizacaoDTO> updateOrganizacao(Authentication authentication, OrganizacaoDTO organizacaoDTO){
            UUID idProprietario = userRepository.findIdByEmail(authentication.getName());
            Organizacao organizacaoExistente = findOrganizacaoOrThrow(organizacaoDTO.getId());

            if (!idProprietario.equals(organizacaoExistente.getProprietario().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            organizacaoExistente.setName(organizacaoDTO.getName());
            organizacaoExistente.setCnpj(organizacaoDTO.getCnpj());
            organizacaoExistente.setDescription(organizacaoDTO.getDescription());

            Organizacao organizacaoSalva = organizacaoRepository.save(organizacaoExistente);

            return ResponseEntity.ok(organizacaoMapper.toDTO(organizacaoSalva));

    }

    public ResponseEntity<Void> deletarOrganizacao(Authentication authentication, UUID organizacaoId) {
            UUID idProprietario = userRepository.findIdByEmail(authentication.getName());
            Organizacao organizacao = findOrganizacaoOrThrow(organizacaoId);

            if (!idProprietario.equals(organizacao.getProprietario().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            organizacaoRepository.delete(organizacao);
            return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<OrganizacaoDTO>> getAllOrganizacoes() {
            List<Organizacao> organizacoes = organizacaoRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(organizacaoMapper.toDTO(organizacoes));
    }

    public ResponseEntity<OrganizacaoDTO> getOrganizacaoById(UUID id) {
            Organizacao organizacao = findOrganizacaoOrThrow(id);
            return ResponseEntity.status(HttpStatus.OK).body(organizacaoMapper.toDTO(organizacao));
    }

    public ResponseEntity<List<OrganizacaoDTO>> getOrganizacaoByName(String name) {
            List<Organizacao> organizacoes = organizacaoRepository.findAllByNameContaining(name);
            return ResponseEntity.ok(organizacaoMapper.toDTO(organizacoes));
    }

}