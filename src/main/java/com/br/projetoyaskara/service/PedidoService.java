package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.response.PedidoResponseDTO;
import com.br.projetoyaskara.model.*;
import com.br.projetoyaskara.model.clientuser.ClientUser;
import com.br.projetoyaskara.repository.CarrinhoRepository;
import com.br.projetoyaskara.repository.ItemCarrinhoRepository;
import com.br.projetoyaskara.repository.PedidoRepository;
import com.br.projetoyaskara.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final UserRepository userRepository;
    private final ItemCarrinhoRepository itemCarrinhoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                         CarrinhoRepository carrinhoRepository,
                         UserRepository userRepository,
                         ItemCarrinhoRepository itemCarrinhoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.userRepository = userRepository;
        this.itemCarrinhoRepository = itemCarrinhoRepository;
    }

    @Transactional
    public ResponseEntity<PedidoResponseDTO> criarPedidoDoCarrinho(Authentication authentication, TransacaoPagamento.MetodoPagamento metodoPagamento) {
        UUID idUser = userRepository.findIdByEmail(authentication.getName());

        ClientUser clientUser = userRepository.findById(idUser)
                .orElseThrow(() -> new IllegalStateException("Cliente não encontrado."));

        Carrinho carrinho = carrinhoRepository.findByClientUserId(idUser);

        if (carrinho == null || carrinho.getItensCarrinho() == null || carrinho.getItensCarrinho().isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio.");
        }

        Pedido novoPedido = new Pedido();

        novoPedido.setClientUser(clientUser);

        List<ItemPedido> itensPedido = new ArrayList<>();
        int valorTotalCentavos = 0;


        for (ItemCarrinho itemCarrinho : carrinho.getItensCarrinho()) {
            int valorItem = itemCarrinho.getLotesIngresso().getValor();
            for (int i = 0; i < itemCarrinho.getQuantidade(); i++) {
                ItemPedido itemPedido = new ItemPedido(novoPedido, itemCarrinho.getLotesIngresso(), valorItem);
                itensPedido.add(itemPedido);
                valorTotalCentavos += valorItem;
            }
        }

        novoPedido.setItensPedido(itensPedido);
        novoPedido.setValorTotal(valorTotalCentavos);

        TransacaoPagamento transacao = new TransacaoPagamento();
        transacao.setPedido(novoPedido);
        transacao.setValorTotal(valorTotalCentavos);
        transacao.setMetodoPagamento(metodoPagamento);

        novoPedido.setTransacaoPagamento(transacao);
        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);

        List<ItemCarrinho> itemsToDelete = new ArrayList<>(carrinho.getItensCarrinho());
        carrinho.getItensCarrinho().clear();
        carrinho.setValorTotal(0);
        itemCarrinhoRepository.deleteAllInBatch(itemsToDelete);
        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok(new PedidoResponseDTO(pedidoSalvo));
    }
}