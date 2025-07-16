package com.br.projetoyaskara.dto.request;

import com.br.projetoyaskara.model.TransacaoPagamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequestDTO {

    UUID idUser;
    TransacaoPagamento.MetodoPagamento metodoPagamento;
}
