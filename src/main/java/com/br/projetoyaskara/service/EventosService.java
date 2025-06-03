package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.EventosDTO;
import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.repository.AvaliacoesEventosRepository;
import com.br.projetoyaskara.repository.EnderecoRepository;
import com.br.projetoyaskara.repository.EventosRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.br.projetoyaskara.util.Utils.atualizarEndereco;


@Service
public class EventosService {

    private final EventosRepository eventosRepository;
    private final EnderecoRepository enderecoRepository;
    private final AvaliacoesEventosRepository avaliacoesEventosRepository;

    public EventosService(EventosRepository eventosRepository, EnderecoRepository enderecoRepository, AvaliacoesEventosRepository avaliacoesEventosRepository) {
        this.eventosRepository = eventosRepository;
        this.enderecoRepository = enderecoRepository;
        this.avaliacoesEventosRepository = avaliacoesEventosRepository;
    }

    public ResponseEntity<?> cadastrarEvento(Eventos eventos) {
        if (eventos.getEndereco() != null) {
            enderecoRepository.save(eventos.getEndereco());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(eventosRepository.save(eventos));
    }

    public ResponseEntity<?> atualizarEvento(Eventos eventos) {
        Eventos eventoAtualizado = eventosRepository.findEventosById(eventos.getId());
        if (eventoAtualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe evento com esse id: " + eventos.getId());
        }

        if (eventoAtualizado.getEndereco() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Endereço invalido");
        }

        eventoAtualizado.setId(eventos.getId());
        eventoAtualizado.setDescricao(eventos.getDescricao());
        eventoAtualizado.setDataInicio(eventos.getDataInicio());
        eventoAtualizado.setDataFim(eventos.getDataFim());
        eventoAtualizado.setFaixaEtaria(eventos.getFaixaEtaria());

        atualizarEndereco(eventoAtualizado.getEndereco(), eventos.getEndereco());
        eventosRepository.save(eventoAtualizado);
        enderecoRepository.save(eventos.getEndereco());
        return ResponseEntity.status(HttpStatus.OK).body(toDto(eventos));

    }

    public ResponseEntity<?> listarEventos() {
        List<Eventos> eventos = eventosRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body((eventos.stream()
                .map(this::toDto)
                .toList()));
    }

    public ResponseEntity<?> deletarEvento(long id) {
        if (!eventosRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe evento com esse id: " + id);
        }
        eventosRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> buscarEventoPorId(long id) {
        Eventos eventos = eventosRepository.findEventosById(id);
        if (eventos == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Evento não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(new EventosDTO(eventos));
    }

    public ResponseEntity<?> buscarEventosPorNomeDaOrganizacao(String nomeDaOrganizacao) {
        List<Eventos> eventos = eventosRepository.findAllByOrganizacao_Name(nomeDaOrganizacao);
        if (eventos == null || eventos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe eventos com o nome dessa organização");
        }
        return ResponseEntity.status(HttpStatus.OK).body(eventos.stream()
                .map(EventosDTO::new)
                .toList());
    }

    public ResponseEntity<?> buscarEventosPorDescricao(String descricao) {
        List<Eventos> eventos = eventosRepository.findAllByDescricaoContaining(descricao);
        if (eventos == null || eventos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum evento com essa descricao");
        }
        return ResponseEntity.status(HttpStatus.OK).body(eventos.stream()
                .map(EventosDTO::new)
                .toList());
    }

    public ResponseEntity<?> buscarEventosPorOrganizacaoId(UUID organizacaoId) {
        try {
            List<Eventos> eventos = eventosRepository.findAllByOrganizacao_Id(organizacaoId);
            if (eventos == null || eventos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não existe eventos, vefique o ID da organização");
            }
            return ResponseEntity.status(HttpStatus.OK).body(eventos.stream()
                    .map(EventosDTO::new)
                    .toList());
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
            return ResponseEntity.status(HttpStatus.OK).body(eventos.stream()
                    .map(EventosDTO::new)
                    .toList());
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
            return ResponseEntity.status(HttpStatus.OK).body(eventos.stream()
                    .map(EventosDTO::new)
                    .toList());
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inconsistência na busca por status");
        }

    }

    public ResponseEntity<?> notaMediaEvento(long eventoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Objects.requireNonNullElse(avaliacoesEventosRepository.notaMediaEvento(eventoId), 0.0));
    }

    private EventosDTO toDto(Eventos evento) {
        return new EventosDTO(evento);
    }
}
