package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Campeonato;
import com.example.scaapi.model.entity.Lesao;
import com.example.scaapi.model.entity.Partida;
import com.example.scaapi.model.entity.Temporada;
import com.example.scaapi.model.repository.CampeonatoRepository;
import com.example.scaapi.model.repository.LesaoRepository;
import com.example.scaapi.model.repository.PartidaRepository;
import com.example.scaapi.model.repository.TemporadaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TemporadaService {

    private TemporadaRepository repository;

    public TemporadaService(TemporadaRepository repository){
        this.repository = repository;
    }

    public List<Temporada> getTemporada() {
        return repository.findAll();
    }

    public Optional<Temporada> getTemporadaById (Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Temporada salvar(Temporada temporada) {
        validar(temporada);
        return repository.save(temporada);
    }

    @Transactional
    public void excluir(Temporada temporada) {
        Objects.requireNonNull(temporada.getId());
        repository.delete(temporada);
    }

    public void validar(Temporada temporada) {
        if (temporada.getDescricao() == null || temporada.getDescricao().trim().equals("")) {
            throw new RegraNegocioException("Descricao inválido");
        }
    }
}
