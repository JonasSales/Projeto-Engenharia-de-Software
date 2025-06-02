package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.LotesIngressoDTO;
import com.br.projetoyaskara.model.LotesIngresso;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.LotesIngressosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotesIngressosService {

    private final LotesIngressosRepository lotesIngressosRepository;
    private final EventosRepository eventosRepository;

    public LotesIngressosService(LotesIngressosRepository lotesIngressosRepository, EventosRepository eventosRepository) {
        this.lotesIngressosRepository = lotesIngressosRepository;
        this.eventosRepository = eventosRepository;
    }

    public ResponseEntity<?> cadastrarIngresso(LotesIngresso lotesIngresso ) {
        if (lotesIngresso == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (eventosRepository.findEventosById(lotesIngresso.getEvento().getId()) != null) {
            LotesIngresso ingressoSalvo = lotesIngressosRepository.save(lotesIngresso);

            return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(ingressoSalvo));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    public ResponseEntity<?> atualizarIngresso(LotesIngressoDTO lotesIngresso){
        LotesIngresso lotesIngressoSalvo = lotesIngressosRepository.findLotesIngressoById(lotesIngresso.getId());
        if (lotesIngressoSalvo == null|| eventosRepository.findEventosById(lotesIngresso.getIdEvento()) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verifique as informações sobre o ID do" +
                    "Ingresso ou o ID do Evento");
        }

        lotesIngressoSalvo.setId(lotesIngresso.getId());
        lotesIngressoSalvo.setName(lotesIngresso.getName());
        lotesIngressoSalvo.setValor(lotesIngresso.getValor());
        lotesIngressoSalvo.setTotalIngressos(lotesIngresso.getTotalIngressos());
        lotesIngressoSalvo.setDataInicio(lotesIngresso.getDataInicio());
        lotesIngressoSalvo.setDataFim(lotesIngresso.getDataFim());
        lotesIngressosRepository.save(lotesIngressoSalvo);

        return ResponseEntity.status(HttpStatus.OK).body(toDTO(lotesIngressoSalvo));
    }

    public ResponseEntity<?> deletarIngresso(LotesIngressoDTO lotesIngresso){

        if (!lotesIngressosRepository.existsById(lotesIngresso.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verifique se este lote existe");
        }
        else if(!eventosRepository.existsById(lotesIngresso.getIdEvento())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verifique se evento existe");
        }

        eventosRepository.deleteById(lotesIngresso.getIdEvento());
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> listarIngressos(){
        List<LotesIngresso> lotesIngressos = lotesIngressosRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(lotesIngressos.stream()
                .map(this::toDTO)
                .toList());
    }

    public ResponseEntity<?> buscarIngressoPorId(Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(toDTO(lotesIngressosRepository.findLotesIngressoById(id)));
    }

    public ResponseEntity<?> buscarIngressosPorEventoId(Long eventoId) {
        LotesIngresso lotesIngresso = lotesIngressosRepository.findLotesIngressoByEventoId(eventoId);
        if (lotesIngresso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe este evento, por favor se" +
                    " certifique se ele realmente existe.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(toDTO(lotesIngresso));
    }

    public ResponseEntity<?> buscarIngressosPorFaixaDePreco(int faixaMenorPreco, int faixaMaiorPreco){
        List<LotesIngresso> lotesIngressos = lotesIngressosRepository.findLotesIngressosPorFaixaDePreco(faixaMenorPreco, faixaMaiorPreco);
        return ResponseEntity.status(HttpStatus.OK).body(lotesIngressos.stream()
                .map(LotesIngressoDTO::new)
                .toList());
    }

    private LotesIngressoDTO toDTO(LotesIngresso lotesIngresso) {
        return new LotesIngressoDTO(lotesIngresso);
    }


}
