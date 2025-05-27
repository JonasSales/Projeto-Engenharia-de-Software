package com.br.projetoyaskara.service;

import com.br.projetoyaskara.repository.LotesIngressosRepository;
import org.springframework.stereotype.Service;

@Service
public class LotesIngressosService {

    private final LotesIngressosRepository lotesIngressosRepository;


    public LotesIngressosService(LotesIngressosRepository lotesIngressosRepository) {
        this.lotesIngressosRepository = lotesIngressosRepository;
    }


}
