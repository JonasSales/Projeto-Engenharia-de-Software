package com.br.projetoyaskara.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.br.projetoyaskara.model.ClientUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(ClientUser user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("auth")
                    .withSubject(user.getEmail())
                    .withExpiresAt(DateExpiration())
                    .sign(algorithm);
        }
        catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException e) {
            throw new RuntimeException("Token expirado", e);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Assinatura inválida", e);
        } catch (AlgorithmMismatchException e) {
            throw new RuntimeException("Algoritmo do token não corresponde", e);
        } catch (InvalidClaimException e) {
            throw new RuntimeException("Claims inválidas no token", e);
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Erro ao verificar token", e);
        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado ao validar token", e);
        }
    }

    private Instant DateExpiration() {
        return LocalDateTime.now().plusMinutes(3600).toInstant(ZoneOffset.of("-03:00"));
    }
}
