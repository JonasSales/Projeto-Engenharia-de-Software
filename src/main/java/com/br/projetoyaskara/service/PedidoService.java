package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.response.PedidoResponseDTO;
import com.br.projetoyaskara.model.*;
import com.br.projetoyaskara.repository.CarrinhoRepository;
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

    public PedidoService(PedidoRepository pedidoRepository,
                         CarrinhoRepository carrinhoRepository,
                         UserRepository userRepository){
        this.pedidoRepository = pedidoRepository;
        this.carrinhoRepository = carrinhoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<PedidoResponseDTO> criarPedidoDoCarrinho(Authentication authentication, TransacaoPagamento.MetodoPagamento metodoPagamento) {
        UUID idUser = userRepository.findIdByEmail(authentication.getName());
        Carrinho carrinho = carrinhoRepository.findByClientUserId(idUser);

        if (carrinho.getItensCarrinho() == null || carrinho.getItensCarrinho().isEmpty()) {
            throw new IllegalStateException("O carrinho está vazio.");
        }

        Pedido novoPedido = new Pedido();
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

        carrinho.getItensCarrinho().clear();
        carrinhoRepository.save(carrinho);

        return ResponseEntity.ok(new PedidoResponseDTO(pedidoSalvo));
    }

    @Transactional
    public ResponseEntity<PedidoResponseDTO> atualizarStatusPagamento(UUID pedidoId,
                                                                      TransacaoPagamento.StatusPagamento novoStatusPagamento) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        TransacaoPagamento transacao = pedido.getTransacaoPagamento();
        transacao.setStatus(novoStatusPagamento);

        switch (novoStatusPagamento) {
            case PAGO:
                pedido.setStatus(Pedido.StatusPedido.PAGO);
                transacao.setDataDePagamento(java.time.LocalDateTime.now());
                break;
            case FALHOU:
                pedido.setStatus(Pedido.StatusPedido.CANCELADO);
                break;
            case REEMBOLSADO:
                pedido.setStatus(Pedido.StatusPedido.REEMBOLSADO);
                transacao.setDataDeReembolso(java.time.LocalDateTime.now());
                break;
            case PENDENTE:
                pedido.setStatus(Pedido.StatusPedido.AGUARDANDO_PAGAMENTO);
                break;
        }
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        return ResponseEntity.ok(new PedidoResponseDTO(pedidoSalvo));
    }
}