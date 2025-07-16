package com.br.projetoyaskara.controller;


import com.br.projetoyaskara.dto.request.UserCreateRequestDTO;
import com.br.projetoyaskara.dto.request.UserUpdateRequestDTO;
import com.br.projetoyaskara.dto.response.UserResponseDTO;
import com.br.projetoyaskara.exception.ConflictException;
import com.br.projetoyaskara.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<UserResponseDTO> getUserProfile(Authentication authentication) {
        return userService.getProfile(authentication);
    }

    @PostMapping()
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserCreateRequestDTO clientUser) throws MessagingException, UnsupportedEncodingException {
        return userService.RegisterUser(clientUser);
    }

    @DeleteMapping()
    public ResponseEntity<String> deletarUser(Authentication authentication) {
        return userService.deletarUser(authentication);
    }

    @PutMapping()
    public ResponseEntity<UserResponseDTO> atualizarUser(Authentication authentication,
                                                         @Valid @RequestBody UserUpdateRequestDTO clientUserDTO) {
        return userService.atualizarUser(authentication ,clientUserDTO);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@Param("code") String code) throws ConflictException {
        return userService.verifyUser(code);
    }

    @PostMapping("/change")
    public ResponseEntity<String> changeUserToOrganization(Authentication authentication) {
        return userService.changeToOrganization(authentication);
    }

}