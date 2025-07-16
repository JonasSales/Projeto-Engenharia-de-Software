package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.request.ItemCarrinhoRequest;
import com.br.projetoyaskara.dto.response.CarrinhoResponse;
import com.br.projetoyaskara.service.CarrinhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @GetMapping()
    public ResponseEntity<CarrinhoResponse> buscarCarrinho(Authentication auth) {
        return carrinhoService.buscarCarrinho(auth);
    }

    @PostMapping()
    public ResponseEntity<CarrinhoResponse> adicionarItemCarrinho(Authentication authentication,
                                                                  @RequestBody ItemCarrinhoRequest itemCarrinhoRequest) {
        return carrinhoService.adicionarItemCarrinho(authentication,itemCarrinhoRequest);
    }

    @DeleteMapping("/{itemCarrinhoId}")
    public ResponseEntity<String> removerItemCarrinho(Authentication authentication,
                                                                @PathVariable UUID itemCarrinhoId){
        return carrinhoService.removerItemCarrinho(authentication, itemCarrinhoId);
    }
}
