package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.EventosDTO;
import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.repository.EnderecoRepository;
import com.br.projetoyaskara.repository.EventosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class EventosService {

    private final EventosRepository eventosRepository;
    private final EnderecoRepository enderecoRepository;

    public EventosService(EventosRepository eventosRepository, EnderecoRepository enderecoRepository) {
        this.eventosRepository = eventosRepository;
        this.enderecoRepository = enderecoRepository;
    }

    public ResponseEntity<?> cadastrarEvento(Eventos eventos) {
        if (eventos.getEndereco() != null) {
            enderecoRepository.save(eventos.getEndereco());
        }
        return new ResponseEntity<>(eventosRepository.save(eventos), HttpStatus.CREATED);
    }

    public ResponseEntity<?> listarEventos() {
        List<Eventos> eventos = eventosRepository.findAll();
        List<EventosDTO> dtos = eventos.stream()
                .map(EventosDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> buscarEventoPorId(long id) {
        Eventos eventos = eventosRepository.findEventosById(id);
        if (eventos == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento não encontrado");
        }
        return ResponseEntity.ok().body(new EventosDTO(eventos));
    }

    public ResponseEntity<?> buscarEventosPorNomeDaOrganizacao(String nomeDaOrganizacao) {
        List<Eventos> eventos = eventosRepository.findAllByOrganizacao_Name(nomeDaOrganizacao);
        if (eventos == null || eventos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe eventos com o nome dessa organização");
        }
        List<EventosDTO> dtos = eventos.stream()
                .map(EventosDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }


    public ResponseEntity<?> buscarEventosPorDescricao(String descricao) {
        List<Eventos> eventos = eventosRepository.findAllByDescricaoContaining(descricao);
        if (eventos == null || eventos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum evento com essa descricao");
        }
        List<EventosDTO> dtos = eventos.stream()
                .map(EventosDTO::new)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> buscarEventosPorOrganizacaoId(UUID organizacaoId) {
        try {
            List<Eventos> eventos = eventosRepository.findAllByOrganizacao_Id(organizacaoId);
            if (eventos == null || eventos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe eventos, vefique o ID da organização");
            }
            List<EventosDTO> dtos = eventos.stream()
                    .map(EventosDTO::new)
                    .toList();
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado");
        }

    }

    public ResponseEntity<?> buscarEventosPorFaixaEtaria(String faixaEtaria) {
        try {
            Eventos.FaixaEtaria faixa = Eventos.FaixaEtaria.valueOf(faixaEtaria);
            List<Eventos> eventos = eventosRepository.findAllByFaixaEtaria(faixa);
            if (eventos == null || eventos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há eventos com essa faixa etária");
            }
            List<EventosDTO> dtos = eventos.stream()
                    .map(EventosDTO::new)
                    .toList();
            return ResponseEntity.ok(dtos);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inconsistência na busca por faixa  etária");
        }
    }

    public ResponseEntity<?> buscarEventosPorStatus(String status) {
        try{
            Eventos.Status sta = Eventos.Status.valueOf(status);
            List<Eventos> eventos = eventosRepository.findAllByStatus(sta);
            if  (eventos == null || eventos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há eventos com esse status");
            }
            List<EventosDTO> dtos = eventos.stream()
                    .map(EventosDTO::new)
                    .toList();
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inconsistência na busca por status");
        }

    }

}
