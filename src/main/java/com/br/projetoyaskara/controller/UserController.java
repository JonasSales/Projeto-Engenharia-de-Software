package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.model.ClientUser;
import com.br.projetoyaskara.service.UserService;
import jakarta.mail.MessagingException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> register(@RequestBody ClientUser clientUser) throws MessagingException, UnsupportedEncodingException {
        return userService.RegisterUser(clientUser);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@Param("code") String code) {
        return userService.verifyUser(code);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.status(HttpStatus.OK).body("HELLO WORLD");
    }
}
