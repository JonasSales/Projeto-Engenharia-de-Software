package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.AuthenticationRequest;
import com.br.projetoyaskara.dto.AuthenticationResponse;
import com.br.projetoyaskara.model.clientuser.ClientUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class LoginService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public LoginService(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping()
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        var userNamePassword = new UsernamePasswordAuthenticationToken(
                authenticationRequest.email(),
                authenticationRequest.password()
        );
        var auth = authenticationManager.authenticate(userNamePassword);
        if (auth.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }
        var token = tokenService.generateToken((ClientUser) auth.getPrincipal());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
