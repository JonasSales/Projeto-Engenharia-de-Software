package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.LotesIngressoDTO;
import com.br.projetoyaskara.exception.BadRequestException;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.LotesIngressoMapper;
import com.br.projetoyaskara.model.LotesIngresso;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.LotesIngressosRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotesIngressosService {

    private final LotesIngressoMapper lotesIngressoMapper;
    private final LotesIngressosRepository lotesIngressosRepository;
    private final EventosRepository eventosRepository;

    public LotesIngressosService(LotesIngressoMapper lotesIngressoMapper, LotesIngressosRepository lotesIngressosRepository, EventosRepository eventosRepository) {
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
        try {

            findEventoOrThrow(lotesIngressoDTO.getIdEvento());

            if (lotesIngressoDTO.getDataInicio().isAfter(lotesIngressoDTO.getDataFim())) {
                throw new BadRequestException("A data de início do lote não pode ser posterior à data de fim.");
            }

            LotesIngresso lotesIngresso = lotesIngressoMapper.toEntity(lotesIngressoDTO);
            LotesIngresso ingressoSalvo = lotesIngressosRepository.save(lotesIngresso);
            return ResponseEntity.status(HttpStatus.CREATED).body(lotesIngressoMapper.toDto(ingressoSalvo));

        } catch (ResourceNotFoundException | BadRequestException e) {

            throw e;
        } catch (DataIntegrityViolationException e) {
            // Captura erros de violação de integridade do banco de dados
            System.err.println("Erro de integridade ao cadastrar ingresso: " + e.getMessage());
            throw new BadRequestException("Erro ao cadastrar ingresso devido a violação de dados (ex: dados duplicados ou inválidos). Detalhes: " + e.getMostSpecificCause().getMessage());
        } catch (Exception e) {
            // Captura outras exceções inesperadas
            System.err.println("Erro inesperado ao cadastrar ingresso: " + e.getMessage());
            throw new RuntimeException("Erro interno ao cadastrar ingresso. Por favor, tente novamente mais tarde.");
        }
    }

    public ResponseEntity<LotesIngressoDTO> atualizarIngresso(LotesIngressoDTO lotesIngressoDTO) {
        try {
            LotesIngresso lotesIngressoExistente = findLotesIngressoOrThrow(lotesIngressoDTO.getId());

            lotesIngressoExistente.setName(lotesIngressoDTO.getName());
            lotesIngressoExistente.setValor(lotesIngressoDTO.getValor());
            lotesIngressoExistente.setTotalIngressos(lotesIngressoDTO.getTotalIngressos());
            lotesIngressoExistente.setDataInicio(lotesIngressoDTO.getDataInicio());
            lotesIngressoExistente.setDataFim(lotesIngressoDTO.getDataFim());

            if (lotesIngressoExistente.getDataInicio().isAfter(lotesIngressoExistente.getDataFim())) {
                throw new BadRequestException("A data de início do lote não pode ser posterior à data de fim.");
            }

            LotesIngresso ingressoAtualizado = lotesIngressosRepository.save(lotesIngressoExistente);

            return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(ingressoAtualizado));
        } catch (ResourceNotFoundException | BadRequestException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            System.err.println("Erro de integridade ao atualizar ingresso: " + e.getMessage());
            throw new BadRequestException("Erro ao atualizar ingresso devido a violação de dados.");
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar ingresso: " + e.getMessage());
            throw new RuntimeException("Erro interno ao atualizar ingresso.");
        }
    }

    public ResponseEntity<Void> deletarIngresso(Long id) {
        try {

            LotesIngresso lotesIngresso = findLotesIngressoOrThrow(id);
            lotesIngressosRepository.delete(lotesIngresso);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao deletar ingresso: " + e.getMessage());
            throw new RuntimeException("Erro interno ao deletar ingresso.");
        }
    }

    public ResponseEntity<List<LotesIngressoDTO>> listarIngressos() {
        try {
            List<LotesIngresso> lotesIngressos = lotesIngressosRepository.findAll();
            if (lotesIngressos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content se não houver ingressos
            }

            return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(lotesIngressos));
        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar ingressos: " + e.getMessage());
            throw new RuntimeException("Erro interno ao listar ingressos.");
        }
    }

    public ResponseEntity<LotesIngressoDTO> buscarIngressoPorId(Long id) {
        try {
            LotesIngresso lotesIngresso = findLotesIngressoOrThrow(id);
            return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(lotesIngresso));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar ingresso por ID: " + e.getMessage());
            throw new RuntimeException("Erro interno ao buscar ingresso.");
        }
    }

    public ResponseEntity<List<LotesIngressoDTO>> buscarIngressosPorEventoId(Long eventoId) {
        try {
            findEventoOrThrow(eventoId);
            List<LotesIngresso> lotesIngresso = lotesIngressosRepository.findLotesIngressoByEventoId(eventoId);

            return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(lotesIngresso));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar ingressos por ID de evento: " + e.getMessage());
            throw new RuntimeException("Erro interno ao buscar ingressos por evento.");
        }
    }

    public ResponseEntity<List<LotesIngressoDTO>> buscarIngressosPorFaixaDePreco(int faixaMenorPreco, int faixaMaiorPreco) {
        try {
            List<LotesIngresso> lotesIngressos = lotesIngressosRepository.findLotesIngressosPorFaixaDePreco(faixaMenorPreco, faixaMaiorPreco);
            return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(lotesIngressos));
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar ingressos por faixa de preço: " + e.getMessage());
            throw new RuntimeException("Erro interno ao buscar ingressos por faixa de preço.");
        }
    }

    public ResponseEntity<?> aumentarVendaEm1(long id){
        LotesIngresso lotesIngresso = findLotesIngressoOrThrow(id);

        lotesIngresso.setTotalVendas(lotesIngresso.getTotalVendas() + 1);
        lotesIngressosRepository.save(lotesIngresso);
        return ResponseEntity.ok().body(lotesIngressoMapper.toDto(lotesIngresso));
    }

}