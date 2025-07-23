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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;
    private final UserRepository userRepository;
    private final LotesIngressosRepository lotesIngressosRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository,
                           ItemCarrinhoRepository itemCarrinhoRepository,
                           UserRepository userRepository,
                           LotesIngressosRepository lotesIngressosRepository) {
        this.carrinhoRepository = carrinhoRepository;
        this.itemCarrinhoRepository = itemCarrinhoRepository;
        this.userRepository = userRepository;
        this.lotesIngressosRepository = lotesIngressosRepository;
    }

    private ClientUser findClientOrThrow(UUID uuid) {
        return userRepository
                .findById(uuid).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    private void recalculateCarrinhoTotal(Carrinho carrinho) {
        int valorTotal = carrinho.getItensCarrinho().stream()
                .mapToInt(item -> item.getLotesIngresso().getValor() * item.getQuantidade())
                .sum();
        carrinho.setValorTotal(valorTotal);
    }

    private Carrinho createNewCarrinhoForUser(ClientUser clientUser) {
        Carrinho carrinho = new Carrinho();
        carrinho.setClientUser(clientUser);
        carrinho.setItensCarrinho(new ArrayList<>());
        carrinho.setValorTotal(0);
        return carrinhoRepository.save(carrinho);
    }

    @Transactional
    public ResponseEntity<CarrinhoResponse> adicionarItemCarrinho(Authentication authentication,
                                                                  ItemCarrinhoRequest itemCarrinhoRequest) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        ClientUser clientUser = findClientOrThrow(clientId);
        Carrinho carrinho = carrinhoRepository.findByClientUserId(clientId);

        if (carrinho == null) {
            carrinho = createNewCarrinhoForUser(clientUser);
        }

        LotesIngresso lote = lotesIngressosRepository.findById(itemCarrinhoRequest.getLotesIngressoId())
                .orElseThrow(() -> new ResourceNotFoundException("Lote de Ingresso não encontrado"));

        Optional<ItemCarrinho> existingItemOpt = carrinho.getItensCarrinho().stream()
                .filter(item -> item.getLotesIngresso().getId().equals(lote.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            ItemCarrinho existingItem = existingItemOpt.get();
            existingItem.setQuantidade(existingItem.getQuantidade() + itemCarrinhoRequest.getQuantidade());
        } else {
            ItemCarrinho novoItem = new ItemCarrinho();
            novoItem.setCarrinho(carrinho);
            novoItem.setLotesIngresso(lote);
            novoItem.setQuantidade(itemCarrinhoRequest.getQuantidade());
            carrinho.getItensCarrinho().add(novoItem);
        }

        recalculateCarrinhoTotal(carrinho);
        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok(new CarrinhoResponse(carrinho));
    }

    public ResponseEntity<CarrinhoResponse> buscarCarrinho(Authentication authentication) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        ClientUser clientUser = findClientOrThrow(clientId);
        Carrinho carrinho = carrinhoRepository.findByClientUserId(clientUser.getId());

        if (carrinho == null) {
            carrinho = createNewCarrinhoForUser(clientUser);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new CarrinhoResponse(carrinho));
    }

    @Transactional
    public ResponseEntity<String> removerItemCarrinho(Authentication authentication, UUID itemCarrinhoId) {
        UUID clientId = userRepository.findIdByEmail(authentication.getName());
        Carrinho carrinho = carrinhoRepository.findByClientUserId(clientId);

        if (carrinho == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrinho não encontrado");
        }

        Optional<ItemCarrinho> itemToRemoveOpt = carrinho.getItensCarrinho().stream()
                .filter(item -> item.getId().equals(itemCarrinhoId))
                .findFirst();

        if (itemToRemoveOpt.isPresent()) {
            ItemCarrinho itemToRemove = itemToRemoveOpt.get();

            carrinho.getItensCarrinho().remove(itemToRemove);
            recalculateCarrinhoTotal(carrinho);
            carrinhoRepository.save(carrinho);
            itemCarrinhoRepository.delete(itemToRemove);

            return ResponseEntity.ok("Item removido");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não encontrado no carrinho");
        }
    }
}