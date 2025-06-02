package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.model.ClientUser;
import com.br.projetoyaskara.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<?> register(@RequestBody ClientUser clientUser) throws MessagingException, UnsupportedEncodingException {
        return userService.RegisterUser(clientUser);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<?> deletarUser(@PathVariable String email) {
        return ResponseEntity.ok().body(userService.deletarUser(email));
    }


    @PutMapping()
    public ResponseEntity<?> atualizarUser( @RequestBody ClientUser clientUser) {
        return userService.atualizarUser(clientUser);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@Param("code") String code) {
        return userService.verifyUser(code);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {return userService.buscarUserPorId(id);}
}
