package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.request.LoteIngressoCreateRequestDTO;
import com.br.projetoyaskara.dto.request.LoteIngressoUpdateRequestDTO;
import com.br.projetoyaskara.dto.response.LoteIngressoResponseDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.model.LotesIngresso;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.LotesIngressosRepository;
import com.br.projetoyaskara.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LotesIngressosService {

    private final LotesIngressosRepository lotesIngressosRepository;
    private final EventosRepository eventosRepository;
    private final UserRepository userRepository;

    public LotesIngressosService(LotesIngressosRepository lotesIngressosRepository,
                                 EventosRepository eventosRepository,
                                 UserRepository userRepository) {

        this.lotesIngressosRepository = lotesIngressosRepository;
        this.eventosRepository = eventosRepository;
        this.userRepository = userRepository;
    }


    private LotesIngresso findLotesIngressoOrThrow(UUID id) {
        return lotesIngressosRepository.
                findById(id).orElseThrow(() -> new ResourceNotFoundException("Ingresso não achado"));
    }

    private Eventos findEventoOrThrow(UUID id) {
        return eventosRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
    }

    public ResponseEntity<LoteIngressoResponseDTO> cadastrarIngresso(Authentication authentication,
                                                              LoteIngressoCreateRequestDTO lotesIngressoDTO) {

        Organizacao organizacao = eventosRepository
                .findByOrganizationIdByEventId(lotesIngressoDTO.getIdEvento());

        Eventos eventos = findEventoOrThrow(lotesIngressoDTO.getIdEvento());

        UUID idUser = userRepository.findIdByEmail(authentication.getName());


        if (!eventosRepository.existsById(lotesIngressoDTO.getIdEvento())
                || !organizacao.getProprietario().getId().equals(idUser))  {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        LotesIngresso lotesIngresso = new LotesIngresso();

        lotesIngresso.setEvento(eventos);
        lotesIngresso.setDataInicio(lotesIngressoDTO.getDataInicio());
        lotesIngresso.setDataFim(lotesIngressoDTO.getDataFim());
        lotesIngresso.setName(lotesIngressoDTO.getName());
        lotesIngresso.setTotalIngressos(lotesIngressoDTO.getTotalIngressos());
        lotesIngresso.setValor(lotesIngressoDTO.getValor());


        LotesIngresso ingressoSalvo = lotesIngressosRepository.save(lotesIngresso);

        return ResponseEntity.status(HttpStatus.CREATED).body(new LoteIngressoResponseDTO(ingressoSalvo));
    }

    public ResponseEntity<LoteIngressoResponseDTO> atualizarIngresso(Authentication authentication,
                                                              LoteIngressoUpdateRequestDTO lotesIngressoDTO) {
            LotesIngresso lotesIngressoExistente = findLotesIngressoOrThrow(lotesIngressoDTO.getId());

            Organizacao organizacao = eventosRepository
                    .findByOrganizationIdByEventId(lotesIngressoExistente.getEvento().getId());

            UUID idUser = userRepository.findIdByEmail(authentication.getName());

            if (!eventosRepository.existsById(lotesIngressoExistente.getEvento().getId())
                || !organizacao.getProprietario().getId().equals(idUser))  {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            lotesIngressoExistente.setName(lotesIngressoDTO.getName());
            lotesIngressoExistente.setValor(lotesIngressoDTO.getValor());
            lotesIngressoExistente.setTotalIngressos(lotesIngressoDTO.getTotalIngressos());
            lotesIngressoExistente.setDataInicio(lotesIngressoDTO.getDataInicio());
            lotesIngressoExistente.setDataFim(lotesIngressoDTO.getDataFim());

            LotesIngresso ingressoAtualizado = lotesIngressosRepository.save(lotesIngressoExistente);
            return ResponseEntity.status(HttpStatus.OK).body(new LoteIngressoResponseDTO(ingressoAtualizado));

    }

    public ResponseEntity<Void> deletarIngresso(Authentication authentication, UUID id) {
        LotesIngresso lotesIngresso = findLotesIngressoOrThrow(id);

        Organizacao organizacao = eventosRepository
                .findByOrganizationIdByEventId(lotesIngresso.getEvento().getId());

        UUID idUser = userRepository.findIdByEmail(authentication.getName());

            if (!eventosRepository.existsById(lotesIngresso.getEvento().getId())
                || !organizacao.getProprietario().getId().equals(idUser))  {
                 return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            lotesIngressosRepository.delete(lotesIngresso);
            return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<LoteIngressoResponseDTO>> listarIngressos() {
        List<LotesIngresso> lotesIngressos = lotesIngressosRepository.findAll();
        if (lotesIngressos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(converterLista(lotesIngressos));
    }

    public ResponseEntity<LoteIngressoResponseDTO> buscarIngressoPorId(UUID id) {
        LotesIngresso lotesIngresso = findLotesIngressoOrThrow(id);
        return ResponseEntity.status(HttpStatus.OK).body(new LoteIngressoResponseDTO(lotesIngresso));
    }

    public ResponseEntity<List<LoteIngressoResponseDTO>> buscarIngressosPorEventoId(UUID eventoId) {
        findEventoOrThrow(eventoId);
        List<LotesIngresso> lotesIngressos = lotesIngressosRepository.findLotesIngressoByEventoId(eventoId);
        return ResponseEntity.status(HttpStatus.OK).body(converterLista(lotesIngressos));
    }

    public ResponseEntity<List<LoteIngressoResponseDTO>> buscarIngressosPorFaixaDePreco(int faixaMenorPreco,
                                                                                        int faixaMaiorPreco) {
        List<LotesIngresso> lotesIngressos = lotesIngressosRepository
                .findLotesIngressosPorFaixaDePreco(faixaMenorPreco,
                                                    faixaMaiorPreco);

        return ResponseEntity.status(HttpStatus.OK).body(converterLista(lotesIngressos));
    }

    private List<LoteIngressoResponseDTO> converterLista(List<LotesIngresso> lotesIngressos) {
        return lotesIngressos.stream().map(LoteIngressoResponseDTO::new).collect(Collectors.toList());
    }
}