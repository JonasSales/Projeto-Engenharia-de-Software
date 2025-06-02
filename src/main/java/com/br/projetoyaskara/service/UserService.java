package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.ClientUserDTO;
import com.br.projetoyaskara.model.ClientUser;
import com.br.projetoyaskara.model.Endereco;
import com.br.projetoyaskara.repository.UserRepository;
import com.br.projetoyaskara.util.GenerateRandonString;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.br.projetoyaskara.repository.EnderecoRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.br.projetoyaskara.util.Utils.atualizarEndereco;

@Service
public class UserService {

    final EnderecoRepository enderecoRepository;
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final MailService mailSender;

    public UserService(EnderecoRepository enderecoRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailSender) {
        this.enderecoRepository = enderecoRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public ResponseEntity<?> RegisterUser(@Valid ClientUser clientUser) throws MessagingException, UnsupportedEncodingException {
        try {
            if (userRepository.findByEmail(clientUser.getUsername()) != null) {
                return  ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já cadastrado");
            }

            if (clientUser.getPassword().length() < 6) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha menor que 6 digitos");
            }
            String encodedPassword = passwordEncoder.encode(clientUser.getPassword());

            clientUser.setPassword(encodedPassword);
            clientUser.setActive(false);
            clientUser.setToken(GenerateRandonString.generateRandomString());

            ClientUserDTO clientUserDTO = new ClientUserDTO(userRepository.save(clientUser));

            mailSender.sendVerificationEmail(clientUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(clientUserDTO);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    @Transactional
    public ResponseEntity<?> deletarUser(String email) {
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe user com esse email: " + email);
        }
        userRepository.deleteClientUserByEmail(email);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> verifyUser(@Valid String verificationToken) {
        ClientUser clientUser = userRepository.findByToken(verificationToken);
        if (clientUser == null || clientUser.isEnabled()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já verificado");
        }
        clientUser.setToken(null);
        clientUser.setActive(true);
        userRepository.save(clientUser);
        return ResponseEntity.status(HttpStatus.OK).body("Verificação realizada com sucesso");
    }

    public ResponseEntity<?> buscarUserPorId(UUID id) {
        return ResponseEntity.ok().body(new ClientUserDTO(userRepository.findClientUserById(id)));
    }

    public ResponseEntity<?> atualizarUser(ClientUser clientUser) {
        ClientUser clientUserAtualizado = userRepository.findByEmail(clientUser.getEmail());
        Endereco enderecoClient = enderecoRepository.findEnderecoById(clientUser.getEndereco().getId());
        if (clientUserAtualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe cliente com esse id: " + clientUser.getId());
        }

        if (enderecoClient == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Endereço invalido");
        }

        String encodedPassword = passwordEncoder.encode(clientUser.getPassword());

        clientUserAtualizado.setEmail(clientUser.getEmail());
        clientUserAtualizado.setRole(clientUser.getRole());
        clientUserAtualizado.setName(clientUser.getName());
        clientUserAtualizado.setModified(LocalDateTime.now());
        clientUserAtualizado.setPassword(encodedPassword);

        atualizarEndereco(clientUserAtualizado.getEndereco(), clientUser.getEndereco());
        userRepository.save(clientUserAtualizado);
        enderecoRepository.save(clientUser.getEndereco());
        return ResponseEntity.status(HttpStatus.OK).body(toDto(clientUser));
    }

    private ClientUserDTO toDto(ClientUser clientUser) {
        return new ClientUserDTO(clientUser);
    }
}

//private UUID id;
//
//    private String name;
//
//    private String email;
//
//    private String role;
//
//    private LocalDateTime created;
//
//    private LocalDateTime modified;
//
//    private boolean active;