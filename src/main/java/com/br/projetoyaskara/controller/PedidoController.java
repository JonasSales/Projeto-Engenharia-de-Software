package com.br.projetoyaskara.controller;

import com.br.projetoyaskara.dto.request.PedidoRequestDTO;
import com.br.projetoyaskara.dto.response.PedidoResponseDTO;
import com.br.projetoyaskara.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido(Authentication authentication, @RequestBody PedidoRequestDTO request) {
            return pedidoService.criarPedidoDoCarrinho(authentication, request.getMetodoPagamento());
    }

}