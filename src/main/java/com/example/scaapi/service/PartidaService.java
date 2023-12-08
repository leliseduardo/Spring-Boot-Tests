package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Lesao;
import com.example.scaapi.model.entity.Partida;
import com.example.scaapi.model.repository.LesaoRepository;
import com.example.scaapi.model.repository.PartidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PartidaService {

    private PartidaRepository repository;

    public PartidaService(PartidaRepository repository){
        this.repository = repository;
    }

    public List<Partida> getPartida() {
        return repository.findAll();
    }

    public Optional<Partida> getPartidaById (Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Partida salvar(Partida partida) {
        validar(partida);
        return repository.save(partida);
    }

    @Transactional
    public void excluir(Partida partida) {
        Objects.requireNonNull(partida.getId());
        repository.delete(partida);
    }

    public void validar(Partida partida) {
        if (partida.getCampeonato() == null || partida.getCampeonato().getId() == null || partida.getCampeonato().getId() == 0) {
            throw new RegraNegocioException("Campeonato inválido");
        }

        if (partida.getEstadio() == null || partida.getEstadio().getId() == null || partida.getEstadio().getId() == 0) {
            throw new RegraNegocioException("Estadio inválido");
        }
    }
}
