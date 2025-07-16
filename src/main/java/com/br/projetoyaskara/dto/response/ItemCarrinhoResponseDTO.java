package com.br.projetoyaskara.dto.response;

import java.util.UUID;

public record ItemCarrinhoResponseDTO(
        UUID id,
        UUID loteIngressoId,
        String nomeIngresso,
        int valor
) {

}