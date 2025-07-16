package com.br.projetoyaskara.controller;


import com.br.projetoyaskara.dto.request.AuthenticationRequest;
import com.br.projetoyaskara.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping()
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        return loginService.login(authenticationRequest);
    }
}
