package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.ClientUserDTO;
import com.br.projetoyaskara.model.ClientUser;
import com.br.projetoyaskara.repository.UserRepository;
import com.br.projetoyaskara.util.GenerateRandonString;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.br.projetoyaskara.repository.EnderecoRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
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

    public ResponseEntity<?> RegisterUser(@Valid ClientUser user) throws MessagingException, UnsupportedEncodingException {
        try {
            if (userRepository.findByEmail(user.getUsername()) != null) {
                return  ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já cadastrado");
            }

            if (user.getPassword().length() < 6) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha menor que 6 digitos");
            }
            String encodedPassword = passwordEncoder.encode(user.getPassword());

            user.setPassword(encodedPassword);
            user.setActive(false);
            user.setToken(GenerateRandonString.generateRandomString());

            ClientUserDTO clientUserDTO = new ClientUserDTO(userRepository.save(user));

            mailSender.sendVerificationEmail(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(clientUserDTO);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }

    public ResponseEntity<?> verifyUser(@Valid String verificationToken) {
        ClientUser user = userRepository.findByToken(verificationToken);
        if (user == null || user.isEnabled()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já verificado");
        }
        user.setToken(null);
        user.setActive(true);
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body("Verificação realizada com sucesso");
    }

    public ResponseEntity<?> buscarUserPorId(UUID id) {
        return ResponseEntity.ok().body(new ClientUserDTO(userRepository.findClientUserById(id)));
    }

    public ResponseEntity<?> atualizarUser(ClientUser clientUser) {
        ClientUser clientUserAtualizado = userRepository.findClientUserById(clientUser.getId());
        if (clientUserAtualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe cliente com esse id: " + clientUser.getId());
        }

        //pode alguém querer remover o endereço
        if (clientUserAtualizado.getEndereco() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Endereço invalido");
        }

        clientUserAtualizado.setEmail(clientUser.getEmail());
        clientUserAtualizado.setRole(clientUser.getRole());
        clientUserAtualizado.setCreated(clientUser.getCreated());
        clientUserAtualizado.setModified(clientUser.getModified());
        clientUserAtualizado.setActive(clientUser.isActive());
        clientUserAtualizado.setPassword(clientUser.getPassword());

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