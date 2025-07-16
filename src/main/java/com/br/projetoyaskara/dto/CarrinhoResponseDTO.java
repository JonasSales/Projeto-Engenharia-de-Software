package com.br.projetoyaskara.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public record CarrinhoResponseDTO(
        UUID id,
        UUID clientUserId,
        List<ItemCarrinhoResponseDTO> itens,
        BigDecimal valorTotal
) {
}