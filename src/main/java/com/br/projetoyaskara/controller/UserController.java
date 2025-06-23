package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.ClientUserDTO;
import com.br.projetoyaskara.exception.ConflictException;
import com.br.projetoyaskara.model.clientuser.ClientUser;
import com.br.projetoyaskara.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<ClientUserDTO> getUserProfile(Authentication authentication) {
        return userService.getProfile(authentication);
    }

    @PostMapping()
    public ResponseEntity<ClientUserDTO> register(@Valid @RequestBody ClientUser clientUser) throws ConflictException {
        return userService.RegisterUser(clientUser);
    }

    @DeleteMapping()
    public ResponseEntity<String> deletarUser(Authentication authentication) {
        return userService.deletarUser(authentication);
    }

    @PutMapping()
    public ResponseEntity<ClientUserDTO> atualizarUser(Authentication authentication ,@Valid @RequestBody ClientUserDTO clientUserDTO) {
        return userService.atualizarUser(authentication ,clientUserDTO);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@Param("code") String code) throws ConflictException {
        return userService.verifyUser(code);
    }



}