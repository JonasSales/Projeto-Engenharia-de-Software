package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.request.ItemCarrinhoRequest;
import com.br.projetoyaskara.dto.response.CarrinhoResponse;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.model.Carrinho;
import com.br.projetoyaskara.model.ItemCarrinho;
import com.br.projetoyaskara.model.LotesIngresso;
import com.br.projetoyaskara.model.clientuser.ClientUser;
import com.br.projetoyaskara.repository.CarrinhoRepository;
import com.br.projetoyaskara.repository.ItemCarrinhoRepository;
import com.br.projetoyaskara.repository.LotesIngressosRepository;
import com.br.projetoyaskara.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class CarrinhoService {

    CarrinhoRepository carrinhoRepository;
    ItemCarrinhoRepository itemCarrinhoRepository;
    UserRepository userRepository;
    LotesIngressosRepository lotesIngressosRepository;

    private ClientUser findClientOrThrow(UUID uuid) {
        return userRepository
                .findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public CarrinhoService(CarrinhoRepository carrinhoRepository,
                           ItemCarrinhoRepository itemCarrinhoRepository,
                           UserRepository userRepository,
                           LotesIngressosRepository lotesIngressosRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.itemCarrinhoRepository = itemCarrinhoRepository;
        this.userRepository = userRepository;
        this.lotesIngressosRepository = lotesIngressosRepository;

    }

    private ResponseEntity<CarrinhoResponse> createCarrinho(Authentication authentication){
        Carrinho carrinho = new Carrinho();

        UUID clientId = userRepository.findIdByEmail(authentication.getName());


        carrinho.setClientUser(findClientOrThrow(clientId));
        carrinho.setItensCarrinho(new ArrayList<>());
        carrinho.setValorTotal(0);
        Carrinho carrinhoSalvo = carrinhoRepository.save(carrinho);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CarrinhoResponse(carrinhoSalvo));
    }

    public ResponseEntity<CarrinhoResponse> adicionarItemCarrinho(Authentication authentication,
                                                                  ItemCarrinhoRequest itemCarrinhoRequest){

        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        Carrinho carrinho = carrinhoRepository.findByClientUserId(clientId);

        if (carrinho == null) {
            this.createCarrinho(authentication);
            carrinho = carrinhoRepository.findByClientUserId(clientId);
        }

        ItemCarrinho itemCarrinho = new ItemCarrinho();
        itemCarrinho.setCarrinho(carrinho);

        LotesIngresso lote = lotesIngressosRepository.findById(itemCarrinhoRequest.getLotesIngressoId()).orElseThrow();
        itemCarrinho.setLotesIngresso(lote);
        itemCarrinho.setQuantidade(itemCarrinhoRequest.getQuantidade());

        carrinho.getItensCarrinho().add(itemCarrinho);

        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok(new CarrinhoResponse(carrinho));
    }

    public ResponseEntity<CarrinhoResponse> buscarCarrinho(Authentication authentication) {
        UUID uuid = userRepository.findIdByEmail(authentication.getName());
        Carrinho carrinho = carrinhoRepository.findByClientUserId(findClientOrThrow(uuid).getId());
        if (carrinho == null) {
            return this.createCarrinho(authentication);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CarrinhoResponse(carrinho));
    }

    public ResponseEntity<String> removerItemCarrinho(Authentication authentication,
                                                      UUID itemCarrinhoId) {

        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        Carrinho carrinho = carrinhoRepository.findByClientUserId(clientId);

        if (carrinho != null && carrinho.getClientUser().getId().equals(clientId)) {
            boolean removed = carrinho.getItensCarrinho().removeIf(item -> item.getId().equals(itemCarrinhoId));

            if (removed) {
                carrinhoRepository.save(carrinho);
                itemCarrinhoRepository.deleteById(itemCarrinhoId);
                return ResponseEntity.ok("Item removido");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não encontrado no carrinho");
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
