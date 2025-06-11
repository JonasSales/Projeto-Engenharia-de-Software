package com.br.projetoyaskara.service;

import com.br.projetoyaskara.dto.EventosDTO;
import com.br.projetoyaskara.exception.ResourceNotFoundException;
import com.br.projetoyaskara.mapper.EventosMapper;
import com.br.projetoyaskara.model.Eventos;
import com.br.projetoyaskara.model.Organizacao;
import com.br.projetoyaskara.repository.AvaliacoesEventosRepository;
import com.br.projetoyaskara.repository.EnderecoRepository;
import com.br.projetoyaskara.repository.EventosRepository;
import com.br.projetoyaskara.repository.OrganizacaoRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.br.projetoyaskara.util.Utils.atualizarEndereco;


@Service
public class EventosService {

    private final EventosMapper eventosMapper;
    private final EventosRepository eventosRepository;
    private final EnderecoRepository enderecoRepository;
    private final AvaliacoesEventosRepository avaliacoesEventosRepository;
    private final OrganizacaoRepository organizacaoRepository;

    public EventosService(EventosMapper eventosMapper, EventosRepository eventosRepository, EnderecoRepository enderecoRepository,
                          AvaliacoesEventosRepository avaliacoesEventosRepository, OrganizacaoRepository organizacaoRepository) {
        this.eventosMapper = eventosMapper;
        this.eventosRepository = eventosRepository;
        this.enderecoRepository = enderecoRepository;
        this.avaliacoesEventosRepository = avaliacoesEventosRepository;
        this.organizacaoRepository = organizacaoRepository;
    }

    private Eventos findEventoOrThrow(Long eventoId) {
        return eventosRepository.findById(eventoId).orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado."));}

    private Organizacao findOrganizacaoOrThrow(UUID organizacaoId) {
        return organizacaoRepository.findById(organizacaoId).orElseThrow(() -> new ResourceNotFoundException("Organizaca não encontrada"));
    }

    public ResponseEntity<?> cadastrarEvento(EventosDTO eventosDTO) {
        try {
            Organizacao organizacao = findOrganizacaoOrThrow(eventosDTO.getOrganizacaoId());

            if (eventosDTO.getDataInicio().isAfter(eventosDTO.getDataFim())) {
                throw new BadRequestException("Data de início não pode ser posterior à data de fim.");
            }

            if (eventosDTO.getEndereco() != null) {
                enderecoRepository.save(eventosDTO.getEndereco());
            }

            Eventos evento = eventosMapper.toEntity(eventosDTO);

            evento.setOrganizacao(organizacao);

            Eventos eventoSalvo = eventosRepository.save(evento);

            return ResponseEntity.status(HttpStatus.CREATED).body(eventosMapper.toDTO(eventoSalvo));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            System.err.println("Erro de violação de integridade ao cadastrar evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Não foi possível cadastrar o evento devido a uma violação de dados.");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao cadastrar evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao cadastrar evento.");
        }
    }


    public ResponseEntity<?> atualizarEvento(EventosDTO eventosDTO) {
        try {

            Eventos eventoAtualizado = findEventoOrThrow(eventosDTO.getId());

            if (eventosDTO.getDataInicio().isAfter(eventosDTO.getDataFim())) {
                throw new BadRequestException("Data de início não pode ser posterior à data de fim.");
            }

            eventoAtualizado.setName(eventosDTO.getName());
            eventoAtualizado.setDescricao(eventosDTO.getDescricao());
            eventoAtualizado.setDataInicio(eventosDTO.getDataInicio());
            eventoAtualizado.setDataFim(eventosDTO.getDataFim());
            eventoAtualizado.setFaixaEtaria(eventosDTO.getFaixaEtaria());
            eventoAtualizado.setStatus(eventosDTO.getStatus());

            if (eventoAtualizado.getEndereco() == null) {
                eventoAtualizado.setEndereco(eventosDTO.getEndereco());
            } else {
                atualizarEndereco(eventoAtualizado.getEndereco(), eventosDTO.getEndereco());
            }
            enderecoRepository.save(eventoAtualizado.getEndereco());

            return ResponseEntity.status(HttpStatus.OK).body(
                    eventosMapper.toDTO(eventosRepository.save(eventoAtualizado)));

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            System.err.println("Erro de violação de integridade ao atualizar evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Não foi possível atualizar o evento devido a uma violação de dados.");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao atualizar evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao atualizar evento.");
        }
    }

    public ResponseEntity<?> listarEventos() {
        try {
            List<Eventos> eventos = eventosRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));

        } catch (Exception e) {
            System.err.println("Erro inesperado ao listar eventos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao listar eventos.");
        }
    }

    public ResponseEntity<?> deletarEvento(long id) {
        try {
            findEventoOrThrow(id);
            eventosRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao deletar evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao deletar evento.");
        }
    }

    public ResponseEntity<?> buscarEventoPorId(long id) {
        try {
            Eventos eventos = findEventoOrThrow(id);
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar evento por ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao buscar evento.");
        }
    }

    public ResponseEntity<?> buscarEventosPorNomeDaOrganizacao(String nomeDaOrganizacao) {
        try {
            List<Eventos> eventos = eventosRepository.findAllByOrganizacao_Name(nomeDaOrganizacao);
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar eventos por nome da organização: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao buscar eventos.");
        }
    }

    public ResponseEntity<?> buscarEventosPorDescricao(String descricao) {
        try {
            List<Eventos> eventos = eventosRepository.findAllByDescricaoContaining(descricao);
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar eventos por descrição: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao buscar eventos.");
        }
    }

    public ResponseEntity<?> buscarEventosPorOrganizacaoId(UUID organizacaoId) {
        try {

            findOrganizacaoOrThrow(organizacaoId);

            List<Eventos> eventos = eventosRepository.findAllByOrganizacao_Id(organizacaoId);
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar eventos por ID da organização: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado ao buscar eventos por ID da organização.");
        }
    }

    public ResponseEntity<?> buscarEventosPorFaixaEtaria(String faixaEtaria) {
        try {
            Eventos.FaixaEtaria faixa = Eventos.FaixaEtaria.valueOf(faixaEtaria.toUpperCase());
            List<Eventos> eventos = eventosRepository.findAllByFaixaEtaria(faixa);
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inconsistência na busca por faixa etária. Valores aceitos: " + java.util.Arrays.toString(Eventos.FaixaEtaria.values()));
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar eventos por faixa etária: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao buscar eventos por faixa etária.");
        }
    }

    public ResponseEntity<?> buscarEventosPorStatus(String status) {
        try {
            Eventos.Status sta = Eventos.Status.valueOf(status.toUpperCase());
            List<Eventos> eventos = eventosRepository.findAllByStatus(sta);
            return ResponseEntity.status(HttpStatus.OK).body(eventosMapper.toDTO(eventos));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Inconsistência na busca por status. Valores aceitos: " + java.util.Arrays.toString(Eventos.Status.values()));
        } catch (Exception e) {
            System.err.println("Erro inesperado ao buscar eventos por status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao buscar eventos por status.");
        }
    }

    public ResponseEntity<?> notaMediaEvento(long eventoId) {
        try {
            findEventoOrThrow(eventoId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Objects.requireNonNullElse(avaliacoesEventosRepository.notaMediaEvento(eventoId), 0.0));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao calcular nota média do evento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor ao calcular nota média do evento.");
        }
    }

}