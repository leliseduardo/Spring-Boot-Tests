package com.example.scaapi.service;

import com.example.scaapi.model.entity.Jogador;
import com.example.scaapi.model.repository.JogadorRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

class JogadorServiceTest {

    @Test
    void deveRetornarGetJogadores(){
        Jogador jogador1 = new Jogador();
        jogador1.setNome("Jogador1 Teste");
        Jogador jogador2 = new Jogador();
        jogador2.setNome("Jogador2 teste");
        List<Jogador> jogadores = new ArrayList<Jogador>();
        jogadores.add(jogador1);
        jogadores.add(jogador2);
        JogadorRepository repository = createMock(JogadorRepository.class);
        expect(repository.findAll()).andReturn(jogadores);
        JogadorService jogadorService = new JogadorService(repository);
        replay(repository);

        assertEquals(jogadores, jogadorService.getJogadores());
    }

    @Test
    void deveRetornarJogadorComIdUm(){
        Jogador jogador1 = new Jogador();
        jogador1.setId((long)1);
        Optional<Jogador> jogadorOptional = Optional.ofNullable(jogador1);
        JogadorRepository repository = createMock(JogadorRepository.class);
        if(jogadorOptional.get().getId() == 1)
            expect(repository.findById((long)1)).andReturn(jogadorOptional);
        JogadorService jogadorService = new JogadorService(repository);
        replay(repository);

        assertEquals(jogadorOptional, jogadorService.getJogadorById((long)1));
    }
}

