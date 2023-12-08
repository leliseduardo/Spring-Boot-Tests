package com.example.scaapi.service;

import com.example.scaapi.model.entity.ScoutPartida;
import com.example.scaapi.model.repository.ScoutPartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class ScoutsPartidaService {

    @Autowired
    private ScoutPartidaRepository repository;

    public List<ScoutPartida> getScoutsPartida(){
        return repository.findAll();
    }

    public Optional<ScoutPartida> getScoutsPartidaById(long id){
        return repository.findById(id);
    }

    @Transactional
    public ScoutPartida salvar(@Valid ScoutPartida scoutPartida){
        return repository.save(scoutPartida);
    }

    @Transactional
    public void excluir(ScoutPartida scoutPartida){
        repository.delete(scoutPartida);
    }
}
