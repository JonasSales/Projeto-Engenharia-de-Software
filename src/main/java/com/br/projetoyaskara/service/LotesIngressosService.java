package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.LotesIngressoDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.LotesIngressoMapper;
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

@Service
public class LotesIngressosService {

    private final LotesIngressoMapper lotesIngressoMapper;
    private final LotesIngressosRepository lotesIngressosRepository;
    private final EventosRepository eventosRepository;
    private final UserRepository userRepository;

    public LotesIngressosService(LotesIngressoMapper lotesIngressoMapper,
                                 LotesIngressosRepository lotesIngressosRepository,
                                 EventosRepository eventosRepository, UserRepository userRepository) {
        this.lotesIngressoMapper = lotesIngressoMapper;
        this.lotesIngressosRepository = lotesIngressosRepository;
        this.eventosRepository = eventosRepository;
        this.userRepository = userRepository;
    }


    private LotesIngresso findLotesIngressoOrThrow(Long id) {
        return lotesIngressosRepository.
                findById(id).orElseThrow(() -> new ResourceNotFoundException("Ingresso não achado"));
    }

    private void findEventoOrThrow(Long id) {
        eventosRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
    }

    public ResponseEntity<LotesIngressoDTO> cadastrarIngresso(Authentication authentication,
                                                              LotesIngressoDTO lotesIngressoDTO) {

        Organizacao organizacao = eventosRepository
                .findByOrganizationIdByEventId(lotesIngressoDTO.getIdEvento());

        UUID idUser = userRepository.findIdByEmail(authentication.getName());


        if (!eventosRepository.existsById(lotesIngressoDTO.getIdEvento())
                || !organizacao.getProprietario().getId().equals(idUser))  {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        LotesIngresso lotesIngresso = lotesIngressoMapper.toEntity(lotesIngressoDTO);
        LotesIngresso ingressoSalvo = lotesIngressosRepository.save(lotesIngresso);

        return ResponseEntity.status(HttpStatus.CREATED).body(lotesIngressoMapper.toDto(ingressoSalvo));
    }

    public ResponseEntity<LotesIngressoDTO> atualizarIngresso(Authentication authentication,
                                                              LotesIngressoDTO lotesIngressoDTO) {
            LotesIngresso lotesIngressoExistente = findLotesIngressoOrThrow(lotesIngressoDTO.getId());

            Organizacao organizacao = eventosRepository
                    .findByOrganizationIdByEventId(lotesIngressoDTO.getIdEvento());

            UUID idUser = userRepository.findIdByEmail(authentication.getName());

            if (!eventosRepository.existsById(lotesIngressoDTO.getIdEvento())
                || !organizacao.getProprietario().getId().equals(idUser))  {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            lotesIngressoExistente.setName(lotesIngressoDTO.getName());
            lotesIngressoExistente.setValor(lotesIngressoDTO.getValor());
            lotesIngressoExistente.setTotalIngressos(lotesIngressoDTO.getTotalIngressos());
            lotesIngressoExistente.setDataInicio(lotesIngressoDTO.getDataInicio());
            lotesIngressoExistente.setDataFim(lotesIngressoDTO.getDataFim());

            LotesIngresso ingressoAtualizado = lotesIngressosRepository.save(lotesIngressoExistente);
            return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(ingressoAtualizado));

    }

    public ResponseEntity<Void> deletarIngresso(Authentication authentication, Long id) {
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

    public ResponseEntity<List<LotesIngressoDTO>> buscarIngressosPorFaixaDePreco(int faixaMenorPreco,
                                                                                 int faixaMaiorPreco) {
        List<LotesIngresso> lotesIngressos = lotesIngressosRepository
                .findLotesIngressosPorFaixaDePreco(faixaMenorPreco,
                                                    faixaMaiorPreco);

        return ResponseEntity.status(HttpStatus.OK).body(lotesIngressoMapper.toDto(lotesIngressos));
    }

}