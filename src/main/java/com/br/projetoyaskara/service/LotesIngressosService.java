package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.LotesIngressoDTO;
import com.br.projetoyaskara.exception.BadRequestException;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.LotesIngressoMapper;
import com.br.projetoyaskara.model.LotesIngresso;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.LotesIngressosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotesIngressosService {

    private final LotesIngressoMapper lotesIngressoMapper;
    private final LotesIngressosRepository lotesIngressosRepository;
    private final EventosRepository eventosRepository;

    public LotesIngressosService(LotesIngressoMapper lotesIngressoMapper,
                                 LotesIngressosRepository lotesIngressosRepository,
                                 EventosRepository eventosRepository) {
        this.lotesIngressoMapper = lotesIngressoMapper;
        this.lotesIngressosRepository = lotesIngressosRepository;
        this.eventosRepository = eventosRepository;
    }


    private LotesIngresso findLotesIngressoOrThrow(Long id) {
        return lotesIngressosRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ingresso não achado"));
    }

    private void findEventoOrThrow(Long id) {
        eventosRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
    }

    public ResponseEntity<LotesIngressoDTO> cadastrarIngresso(LotesIngressoDTO lotesIngressoDTO) {

            findEventoOrThrow(lotesIngressoDTO.getIdEvento());

            if (lotesIngressoDTO.getDataInicio().isAfter(lotesIngressoDTO.getDataFim())) {
                throw new BadRequestException("A data de início do lote não pode ser posterior à data de fim.");
            }

            LotesIngresso lotesIngresso = lotesIngressoMapper.toEntity(lotesIngressoDTO);
            LotesIngresso ingressoSalvo = lotesIngressosRepository.save(lotesIngresso);
            return ResponseEntity.status(HttpStatus.CREATED).body(lotesIngressoMapper.toDto(ingressoSalvo));

    }

    public ResponseEntity<LotesIngressoDTO> atualizarIngresso(LotesIngressoDTO lotesIngressoDTO) {
            LotesIngresso lotesIngressoExistente = findLotesIngressoOrThrow(lotesIngressoDTO.getId());

            lotesIngressoExistente.setName(lotesIngressoDTO.getName());
            lotesIngressoExistente.setValor(lotesIngressoDTO.getValor());
            lotesIngressoExistente.setTotalIngressos(lotesIngressoDTO.getTotalIngressos());
            lotesIngressoExistente.setDataInicio(lotesIngressoDTO.getDataInicio());
            lotesIngressoExistente.setDataFim(lotesIngressoDTO.getDataFim());

            LotesIngresso ingressoAtualizado = lotesIngressosRepository.save(lotesIngressoExistente);
            return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(ingressoAtualizado));

    }

    public ResponseEntity<Void> deletarIngresso(Long id) {
            LotesIngresso lotesIngresso = findLotesIngressoOrThrow(id);
            lotesIngressosRepository.delete(lotesIngresso);
            return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<LotesIngressoDTO>> listarIngressos() {
        List<LotesIngresso> lotesIngressos = lotesIngressosRepository.findAll();
        if (lotesIngressos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(lotesIngressos));
    }


    public ResponseEntity<LotesIngressoDTO> buscarIngressoPorId(Long id) {
        LotesIngresso lotesIngresso = findLotesIngressoOrThrow(id);
        return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(lotesIngresso));
    }

    public ResponseEntity<List<LotesIngressoDTO>> buscarIngressosPorEventoId(Long eventoId) {
        findEventoOrThrow(eventoId);
        List<LotesIngresso> lotesIngresso = lotesIngressosRepository.findLotesIngressoByEventoId(eventoId);
        return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(lotesIngresso));
    }

    public ResponseEntity<List<LotesIngressoDTO>> buscarIngressosPorFaixaDePreco(int faixaMenorPreco, int faixaMaiorPreco) {
        List<LotesIngresso> lotesIngressos = lotesIngressosRepository
                .findLotesIngressosPorFaixaDePreco(faixaMenorPreco, faixaMaiorPreco);

        return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(lotesIngressos));
    }

    public ResponseEntity<?> aumentarVendaEm1(long id){
        LotesIngresso lotesIngresso = findLotesIngressoOrThrow(id);

        lotesIngresso.setTotalVendas(lotesIngresso.getTotalVendas() + 1);
        lotesIngressosRepository.save(lotesIngresso);
        return ResponseEntity.ok().body(lotesIngressoMapper.toDto(lotesIngresso));
    }

}