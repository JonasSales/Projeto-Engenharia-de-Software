package com.br.projetoyaskara.service;


import com.br.projetoyaskara.dto.ClientUserDTO;
import com.br.projetoyaskara.model.ClientUser;
import com.br.projetoyaskara.repository.UserRepository;
import com.br.projetoyaskara.util.GenerateRandonString;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class UserService {


    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final MailService mailSender;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public ResponseEntity<?> RegisterUser(@Valid ClientUser user) throws MessagingException, UnsupportedEncodingException {
        if (userRepository.findByEmail(user.getUsername()) != null) {
            return  ResponseEntity.status(HttpStatus.CONFLICT).body("Usuário já cadastrado");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
        user.setActive(false);
        user.setToken(GenerateRandonString.generateRandomString());

        ClientUserDTO clientUserDTO = new ClientUserDTO(userRepository.save(user));

        mailSender.sendVerificationEmail(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientUserDTO);
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
}
