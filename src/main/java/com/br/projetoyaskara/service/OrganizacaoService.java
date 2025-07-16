package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.request.EnderecoRequestDTO;
import com.br.projetoyaskara.dto.request.OrganizacaoCreateRequestDTO;
import com.br.projetoyaskara.dto.request.OrganizacaoUpdateRequestDTO;
import com.br.projetoyaskara.dto.response.OrganizacaoResponseDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
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
import java.util.stream.Collectors;

@Service
public class OrganizacaoService {

    private final OrganizacaoRepository organizacaoRepository;
    private final UserRepository userRepository;

    public OrganizacaoService(OrganizacaoRepository organizacaoRepository,
                              UserRepository userRepository) {
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

    public ResponseEntity<OrganizacaoResponseDTO> registrarOrganizacao(
            Authentication authentication, OrganizacaoCreateRequestDTO organizacaoDTO) {

            UUID idUsuarioAutenticado = userRepository.findIdByEmail(authentication.getName());

            ClientUser clientUser = findClientOrThrow(idUsuarioAutenticado);

            Organizacao novaOrganizacao = new Organizacao();

            novaOrganizacao.setProprietario(clientUser);
            novaOrganizacao.setName(organizacaoDTO.getName());
            novaOrganizacao.setDescription(organizacaoDTO.getDescription());
            novaOrganizacao.setCnpj(organizacaoDTO.getCnpj());



            Organizacao organizacaoSalva = organizacaoRepository.save(novaOrganizacao);

            return ResponseEntity.status(HttpStatus.CREATED).body(new OrganizacaoResponseDTO(organizacaoSalva));
    }


    public ResponseEntity<OrganizacaoResponseDTO> updateOrganizacao(Authentication authentication, OrganizacaoUpdateRequestDTO organizacaoDTO){
            UUID idProprietario = userRepository.findIdByEmail(authentication.getName());
            Organizacao organizacaoExistente = findOrganizacaoOrThrow(organizacaoDTO.getId());

            if (!idProprietario.equals(organizacaoExistente.getProprietario().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            organizacaoExistente.setName(organizacaoDTO.getName());
            organizacaoExistente.setCnpj(organizacaoDTO.getCnpj());
            organizacaoExistente.setDescription(organizacaoDTO.getDescription());

            Organizacao organizacaoSalva = organizacaoRepository.save(organizacaoExistente);

            return ResponseEntity.ok(new OrganizacaoResponseDTO(organizacaoSalva));

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

    public ResponseEntity<List<OrganizacaoResponseDTO>> getAllOrganizacoes() {
            List<Organizacao> organizacoes = organizacaoRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(converterLista(organizacoes));
    }

    public ResponseEntity<OrganizacaoResponseDTO> getOrganizacaoById(UUID id) {
            Organizacao organizacao = findOrganizacaoOrThrow(id);
            return ResponseEntity.status(HttpStatus.OK).body(new OrganizacaoResponseDTO(organizacao));
    }

    public ResponseEntity<List<OrganizacaoResponseDTO>> getOrganizacaoByName(String name) {
            List<Organizacao> organizacoes = organizacaoRepository.findAllByNameContaining(name);
            return ResponseEntity.ok(converterLista(organizacoes));
    }

    public ResponseEntity<List<OrganizacaoResponseDTO>> getOrganizacoesByIdProprietario(Authentication authentication) {
        UUID idProprietario = userRepository.findIdByEmail(authentication.getName());
        List<Organizacao> organizacoes = organizacaoRepository.findAllOrganizationByIdProprietario(idProprietario);

        return  ResponseEntity.ok(converterLista(organizacoes));

    }

    private List<OrganizacaoResponseDTO> converterLista(List<Organizacao> organizacoes) {
        return organizacoes.stream().map(OrganizacaoResponseDTO::new).collect(Collectors.toList());
    }

}