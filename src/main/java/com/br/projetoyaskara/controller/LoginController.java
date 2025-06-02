package com.br.projetoyaskara.controller;


import com.br.projetoyaskara.dto.AuthenticationRequest;
import com.br.projetoyaskara.dto.AuthenticationResponse;
import com.br.projetoyaskara.model.ClientUser;
import com.br.projetoyaskara.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    public LoginController(TokenService tokenService, AuthenticationManager authenticationManager) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping()
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            var userNamePassword = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.email(),
                    authenticationRequest.password()
            );

            var auth = authenticationManager.authenticate(userNamePassword);
            var token = tokenService.generateToken((ClientUser) auth.getPrincipal());

            return ResponseEntity.ok(new AuthenticationResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao autenticar");
        }
    }

}
