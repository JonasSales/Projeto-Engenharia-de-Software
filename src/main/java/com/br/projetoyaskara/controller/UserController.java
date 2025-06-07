package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.ClientUserDTO;
import com.br.projetoyaskara.exception.ConflictException;
import com.br.projetoyaskara.model.ClientUser;
import com.br.projetoyaskara.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<?> register(@Valid @RequestBody ClientUser clientUser) throws ConflictException {
        return userService.RegisterUser(clientUser);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deletarUser(@PathVariable String email) {
        return userService.deletarUser(email);
    }

    @PutMapping()
    public ResponseEntity<?> atualizarUser(@Valid @RequestBody ClientUserDTO clientUserDTO) {
        return userService.atualizarUser(clientUserDTO);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@Param("code") String code) throws ConflictException {
        return userService.verifyUser(code);
    }

}