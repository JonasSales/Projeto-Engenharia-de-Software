package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.request.EnderecoRequestDTO;
import com.br.projetoyaskara.dto.response.EnderecoResponseDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.model.Endereco;
import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.model.clientuser.ClientUser;
import com.br.projetoyaskara.repository.EnderecoRepository;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.OrganizacaoRepository;
import com.br.projetoyaskara.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.br.projetoyaskara.util.Utils.atualizarEndereco;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final UserRepository userRepository;
    private final EventosRepository eventosRepository;
    private final OrganizacaoRepository organizacaoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, UserRepository userRepository,
                           EventosRepository eventosRepository,
                           OrganizacaoRepository organizacaoRepository) {
        this.enderecoRepository = enderecoRepository;
        this.userRepository = userRepository;
        this.eventosRepository = eventosRepository;
        this.organizacaoRepository = organizacaoRepository;
    }

    private Endereco findEnderecoOrThrow(long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereco não encontrado"));
    }

    private Eventos findEventosOrThrow(UUID id) {
        return eventosRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
    }

    private Organizacao findOrganizacaoOrThrow(UUID id) {
        return organizacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organização não encontrada"));
    }

    // ===================== CLIENTE =====================

    public ResponseEntity<EnderecoResponseDTO> cadastrarEnderecoClient(Authentication authentication,
                                                                       EnderecoRequestDTO enderecoDTO) {
        ClientUser clientUser = userRepository.findByEmail(authentication.getName());

        if (clientUser.getEndereco() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Endereco endereco = new Endereco();
        endereco.setLatitude(enderecoDTO.getLatitude());
        endereco.setLongitude(enderecoDTO.getLongitude());
        endereco.setBairro(enderecoDTO.getBairro());
        endereco.setCep(enderecoDTO.getCep());
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setEstado(enderecoDTO.getEstado());
        endereco.setComplemento(enderecoDTO.getComplemento());
        clientUser.setEndereco(endereco);

        userRepository.save(clientUser);
        Endereco enderecoAtualizado = enderecoRepository.save(endereco);

        return ResponseEntity.ok(new EnderecoResponseDTO(enderecoAtualizado));
    }

    public ResponseEntity<EnderecoResponseDTO> atualizarEnderecoClient(Authentication authentication,
                                                                       EnderecoRequestDTO enderecoDTO) {
        ClientUser clientUser = userRepository.findByEmail(authentication.getName());

        if (clientUser.getEndereco() != null) {
            Endereco enderecoDesatualizado = clientUser.getEndereco();
            atualizarEndereco(enderecoDesatualizado, new Endereco(enderecoDTO));
            enderecoRepository.save(enderecoDesatualizado);
            return ResponseEntity.ok(new EnderecoResponseDTO(enderecoDesatualizado));
        } else {
            return cadastrarEnderecoClient(authentication, enderecoDTO);
        }
    }

    public ResponseEntity<EnderecoResponseDTO> buscarEnderecoCLient(Authentication authentication) {
        ClientUser clientUser = userRepository.findByEmail(authentication.getName());
        return ResponseEntity.ok(new EnderecoResponseDTO(clientUser.getEndereco()));
    }

    public ResponseEntity<String> deletarEnderecoClient(Authentication authentication) {
        ClientUser clientUser = userRepository.findByEmail(authentication.getName());

        if (clientUser.getEndereco() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não possui endereço.");
        }

        Endereco endereco = findEnderecoOrThrow(clientUser.getEndereco().getId());
        clientUser.setEndereco(null);
        userRepository.save(clientUser);
        enderecoRepository.delete(endereco);
        return ResponseEntity.ok("Endereço deletado");
    }

    // ===================== EVENTO =====================

    public ResponseEntity<EnderecoResponseDTO> cadastrarEnderecoEvento(Authentication authentication,
                                                                       EnderecoRequestDTO enderecoDTO, UUID idEvento) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        Eventos evento = findEventosOrThrow(idEvento);


        System.out.println("Testando aqui");
        if (!clientId.equals(evento.getOrganizacao().getProprietario().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }



        if (evento.getEndereco() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Endereco endereco = new Endereco(enderecoDTO);
        evento.setEndereco(endereco);
        Eventos eventosSalvo = eventosRepository.save(evento);

        return ResponseEntity.ok(new EnderecoResponseDTO(eventosSalvo.getEndereco()));
    }

    public ResponseEntity<EnderecoResponseDTO> atualizarEnderecoEvento(Authentication authentication, UUID idEvento,
                                                                       EnderecoRequestDTO enderecoDTO) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        Eventos evento = findEventosOrThrow(idEvento);
        UUID organizacaoId = organizacaoRepository.findOrganizacaoByEventosId(idEvento);

        if (!organizacaoId.equals(clientId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (evento.getEndereco() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Endereco enderecoDesatualizado = findEnderecoOrThrow(evento.getEndereco().getId());
        Endereco enderecoAtualizado = new Endereco(enderecoDTO);
        atualizarEndereco(enderecoDesatualizado, enderecoAtualizado);
        enderecoRepository.save(enderecoDesatualizado);

        return ResponseEntity.ok(new EnderecoResponseDTO(enderecoDesatualizado));
    }

    public ResponseEntity<String> deletarEnderecoEvento(Authentication authentication, UUID idEvento) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        UUID organizacaoId = organizacaoRepository.findOrganizacaoByEventosId(idEvento);

        if (!clientId.equals(organizacaoId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado.");
        }

        Eventos evento = findEventosOrThrow(idEvento);
        Endereco endereco = evento.getEndereco();

        if (endereco == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("O evento não possui endereço.");
        }

        evento.setEndereco(null);
        eventosRepository.save(evento);
        enderecoRepository.delete(endereco);

        return ResponseEntity.ok("Endereço do evento deletado");
    }

    public ResponseEntity<EnderecoResponseDTO> buscarEnderecoEvento(Authentication authentication, UUID idEvento) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        UUID organizacaoId = organizacaoRepository.findOrganizacaoByEventosId(idEvento);

        if (!clientId.equals(organizacaoId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Eventos evento = findEventosOrThrow(idEvento);

        if (evento.getEndereco() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(new EnderecoResponseDTO(evento.getEndereco()));
    }

    // ===================== ORGANIZAÇÃO =====================

    public ResponseEntity<EnderecoResponseDTO> cadastrarEnderecoOrganizacao(Authentication authentication,
                                                                            UUID idOrganizacao, EnderecoRequestDTO enderecoDTO) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        Organizacao organizacao = findOrganizacaoOrThrow(idOrganizacao);

        if (!clientId.equals(organizacao.getProprietario().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (organizacao.getEndereco() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Endereco endereco = new Endereco(enderecoDTO);
        enderecoRepository.save(endereco);
        organizacao.setEndereco(endereco);
        organizacaoRepository.save(organizacao);

        return ResponseEntity.ok(new EnderecoResponseDTO(endereco));
    }

    public ResponseEntity<EnderecoResponseDTO> atualizarEnderecoOrganizacao(Authentication authentication,
                                                                            UUID idOrganizacao, EnderecoRequestDTO enderecoDTO) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        Organizacao organizacao = findOrganizacaoOrThrow(idOrganizacao);

        if (!clientId.equals(organizacao.getProprietario().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (organizacao.getEndereco() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Endereco enderecoDesatualizado = findEnderecoOrThrow(organizacao.getEndereco().getId());
        Endereco enderecoAtualizado = new Endereco(enderecoDTO);
        atualizarEndereco(enderecoDesatualizado, enderecoAtualizado);
        enderecoRepository.save(enderecoDesatualizado);

        return ResponseEntity.ok(new EnderecoResponseDTO(enderecoDesatualizado));
    }

    public ResponseEntity<String> deletarEnderecoOrganizacao(Authentication authentication, UUID idOrganizacao) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        Organizacao organizacao = findOrganizacaoOrThrow(idOrganizacao);

        if (!clientId.equals(organizacao.getProprietario().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado: você não é o proprietário da organização.");
        }

        Endereco endereco = organizacao.getEndereco();
        if (endereco == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("A organização não possui endereço.");
        }

        organizacao.setEndereco(null);
        organizacaoRepository.save(organizacao);
        enderecoRepository.delete(endereco);

        return ResponseEntity.ok("Endereço da organização deletado com sucesso.");
    }

    public ResponseEntity<EnderecoResponseDTO> buscarEnderecoOrganizacao(Authentication authentication, UUID idOrganizacao) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        Organizacao organizacao = findOrganizacaoOrThrow(idOrganizacao);

        if (!clientId.equals(organizacao.getProprietario().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (organizacao.getEndereco() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(new EnderecoResponseDTO(organizacao.getEndereco()));
    }
}
