package com.example.scaapi.service;

import com.example.scaapi.model.entity.ScoutJogador;
import com.example.scaapi.model.repository.ScoutJogadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class ScoutsJogadorService {

    @Autowired
    private ScoutJogadorRepository repository;

    public List<ScoutJogador> getScoutsJogador(){
        return repository.findAll();
    }

    public Optional<ScoutJogador> getScoutsJogadorById(long id){
        return repository.findById(id);
    }

    @Transactional
    public ScoutJogador salvar(@Valid ScoutJogador scoutJogador){
        return repository.save(scoutJogador);
    }

    @Transactional
    public void excluir(ScoutJogador scoutJogador){
        repository.delete(scoutJogador);
    }
}
