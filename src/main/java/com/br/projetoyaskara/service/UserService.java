package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.request.UserCreateRequestDTO;
import com.br.projetoyaskara.dto.request.UserUpdateRequestDTO;
import com.br.projetoyaskara.dto.response.UserResponseDTO;
import com.br.projetoyaskara.exception.BadRequestException;
import com.br.projetoyaskara.exception.ConflictException;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.model.clientuser.ClientUser;
import com.br.projetoyaskara.model.clientuser.Role;
import com.br.projetoyaskara.repository.UserRepository;
import com.br.projetoyaskara.util.GenerateRandonString;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailSender;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       MailService mailSender) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }



    private ClientUser findClientUserByEmailOrThrow(String email) {
        ClientUser clientUser = userRepository.findByEmail(email);
        if (clientUser == null) {
            throw new ResourceNotFoundException("Cliente não encontrado");
        }
        return clientUser;
    }

    public ResponseEntity<UserResponseDTO> RegisterUser(@Valid UserCreateRequestDTO userCreateRequestDTO) throws MessagingException,
            UnsupportedEncodingException {

            if (userCreateRequestDTO.getPassword() == null || userCreateRequestDTO.getPassword().length() < 6) {
                throw new BadRequestException("A senha deve ter no mínimo 6 dígitos.");
            }

            ClientUser clientUser = new ClientUser();
            clientUser.setEmail(userCreateRequestDTO.getEmail());
            clientUser.setName(userCreateRequestDTO.getName());
            clientUser.setPassword(passwordEncoder.encode(userCreateRequestDTO.getPassword()));
            clientUser.setRole(Role.USER);
            clientUser.setCreated(LocalDateTime.now());
            clientUser.setModified(LocalDateTime.now());
            clientUser.setActive(false);
            clientUser.setToken(GenerateRandonString.generateRandomString());

            System.out.println(clientUser.getToken());

            ClientUser savedUser = userRepository.save(clientUser);
            mailSender.sendVerificationEmail(savedUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(savedUser));
    }

    @Transactional
    public ResponseEntity<String> deletarUser(Authentication authentication) {
            ClientUser clientUser = findClientUserByEmailOrThrow(authentication.getName());
            userRepository.delete(clientUser);
            return ResponseEntity.status(HttpStatus.OK).body("Conta deletada com sucesso.");
    }

    public ResponseEntity<UserResponseDTO> atualizarUser(Authentication authentication, UserUpdateRequestDTO clientUserDTO) {
            ClientUser clientUserAtualizado = findClientUserByEmailOrThrow(authentication.getName());

            clientUserAtualizado.setName(clientUserDTO.getName());
            clientUserAtualizado.setEmail(clientUserDTO.getEmail());
            clientUserAtualizado.setModified(LocalDateTime.now());
            ClientUser updatedUser = userRepository.save(clientUserAtualizado);
            return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDTO(updatedUser));

    }

    public ResponseEntity<UserResponseDTO> getProfile(Authentication authentication) {
            ClientUser clientUser = findClientUserByEmailOrThrow(authentication.getName());
            return ResponseEntity.ok(new UserResponseDTO(clientUser));
    }

    public ResponseEntity<String> verifyUser(String verificationToken) throws ConflictException {
            ClientUser clientUser = userRepository.findByToken(verificationToken);
            if (clientUser == null) {
                throw new BadRequestException("Token de verificação inválido ou expirado.");
            }
            if (clientUser.isActive()) {
                throw new ConflictException("Usuário já verificado.");
            }
            clientUser.setToken(null);
            clientUser.setActive(true);
            userRepository.save(clientUser);
            return ResponseEntity.status(HttpStatus.OK).body("Verificação realizada com sucesso.");
    }

    public ResponseEntity<String> changeToOrganization(Authentication authentication) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        userRepository.changeRoleAccountToOrganization(clientId);
        return ResponseEntity.ok("Atualizado role com sucesso");

    }
}