package com.br.projetoyaskara.util;


import com.br.projetoyaskara.model.Endereco;


public final class Utils {


    private Utils() {}

    public static void atualizarEndereco(Endereco destino, Endereco origem) {
        destino.setBairro(origem.getBairro());
        destino.setCep(origem.getCep());
        destino.setEstado(origem.getEstado());
        destino.setComplemento(origem.getComplemento());
        destino.setCidade(origem.getCidade());
    }


}
