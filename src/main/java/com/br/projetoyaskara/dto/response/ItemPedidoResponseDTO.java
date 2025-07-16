package com.br.projetoyaskara.dto.response;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ItemPedidoResponseDTO(
        @NotNull UUID id,
        @NotNull UUID pedidoId,
        @NotNull UUID loteIngressoId,
        @NotNull String nomeIngresso,
        int valor
) {

}