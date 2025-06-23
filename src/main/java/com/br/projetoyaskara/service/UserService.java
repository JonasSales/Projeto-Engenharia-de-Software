package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.ClientUserDTO;
import com.br.projetoyaskara.exception.BadRequestException;
import com.br.projetoyaskara.exception.ConflictException;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.ClientUserMapper;
import com.br.projetoyaskara.model.clientuser.ClientUser;
import com.br.projetoyaskara.model.clientuser.Role;
import com.br.projetoyaskara.repository.UserRepository;
import com.br.projetoyaskara.util.GenerateRandonString;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.br.projetoyaskara.repository.EnderecoRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class UserService {

    private final ClientUserMapper clientUserMapper;
    private final EnderecoRepository enderecoRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailSender;

    public UserService(ClientUserMapper clientUserMapper, EnderecoRepository enderecoRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailSender) {
        this.clientUserMapper = clientUserMapper;
        this.enderecoRepository = enderecoRepository;
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

    public ResponseEntity<ClientUserDTO> RegisterUser(ClientUser clientUser) throws ConflictException {
        try {

            if (userRepository.existsByEmail(clientUser.getEmail())) {
                throw new ConflictException("Usuário com este e-mail já cadastrado.");
            }

            if (clientUser.getPassword() == null || clientUser.getPassword().length() < 6) {
                throw new BadRequestException("A senha deve ter no mínimo 6 dígitos.");
            }


            clientUser.setPassword(passwordEncoder.encode(clientUser.getPassword()));
            clientUser.setRole(Role.USER);
            clientUser.setCreated(LocalDateTime.now());
            clientUser.setModified(LocalDateTime.now());
            clientUser.setActive(false);
            clientUser.setToken(GenerateRandonString.generateRandomString());

            if (clientUser.getEndereco() != null) {
                enderecoRepository.save(clientUser.getEndereco());
            }

            ClientUser savedUser = userRepository.save(clientUser);
            mailSender.sendVerificationEmail(savedUser);

            return ResponseEntity.status(HttpStatus.CREATED).body(clientUserMapper.clientUserToClientUserDTO(savedUser));
        } catch (ConflictException | BadRequestException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            System.err.println("Erro de integridade ao registrar usuário: " + e.getMessage());
            throw new BadRequestException("Erro ao registrar usuário: CNPJ, CPF ou e-mail já em uso ou dados inválidos.");
        } catch (Exception e) {
            System.err.println("Erro inesperado ao registrar usuário: " + e.getMessage());
            throw new RuntimeException("Erro interno ao registrar usuário.");
        }
    }

    @Transactional
    public ResponseEntity<String> deletarUser(Authentication authentication) {
        try {
            ClientUser clientUser = findClientUserByEmailOrThrow(authentication.getName());
            userRepository.delete(clientUser);
            return ResponseEntity.status(HttpStatus.OK).body("Conta deletada com sucesso.");
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao deletar usuário: " + e.getMessage());
            throw new RuntimeException("Erro interno ao deletar usuário.");
        }
    }

    public ResponseEntity<String> verifyUser(String verificationToken) throws ConflictException {
        try {
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
        } catch (BadRequestException | ConflictException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao verificar usuário: " + e.getMessage());
            throw new RuntimeException("Erro interno ao verificar usuário.");
        }
    }

    public ResponseEntity<ClientUserDTO> atualizarUser(Authentication authentication,ClientUserDTO clientUserDTO) {
        try {
            ClientUser clientUserAtualizado = findClientUserByEmailOrThrow(authentication.getName());

            clientUserAtualizado.setName(clientUserDTO.getName());
            clientUserAtualizado.setEmail(clientUserDTO.getEmail());
            clientUserAtualizado.setModified(LocalDateTime.now());
            ClientUser updatedUser = userRepository.save(clientUserAtualizado);
            return ResponseEntity.status(HttpStatus.OK).body(clientUserMapper.clientUserToClientUserDTO(updatedUser));
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            System.err.println("Erro de integridade ao atualizar usuário: " + e.getMessage());
            throw new BadRequestException("Erro ao atualizar usuário devido a violação de dados.");
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar usuário: " + e.getMessage());
            throw new RuntimeException("Erro interno ao atualizar usuário.");
        }
    }

    public ResponseEntity<ClientUserDTO> getProfile(Authentication authentication) {
        try {
            ClientUser clientUser = findClientUserByEmailOrThrow(authentication.getName());
            return ResponseEntity.ok(clientUserMapper.clientUserToClientUserDTO(clientUser));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar perfil do usuário: " + e.getMessage());
            throw new RuntimeException("Erro interno ao buscar perfil do usuário.");
        }
    }
}