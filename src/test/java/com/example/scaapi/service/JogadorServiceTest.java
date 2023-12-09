package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Jogador;
import com.example.scaapi.model.entity.Posicao;
import com.example.scaapi.model.repository.JogadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

class JogadorServiceTest {

    private Jogador createJogadorTest(){
        Posicao posicao = new Posicao();
        List<Posicao> posicoes = new ArrayList<Posicao>();
        posicoes.add(posicao);
        Jogador jogador = new Jogador();
        jogador.setId((long)1);
        jogador.setAtivo(true);
        jogador.setNome("Jogador teste");
        jogador.setEmail("email@email.com");
        jogador.setLogin("login teste");
        jogador.setDataNascimento(LocalDate.of(1990, 01, 15));
        jogador.setCpf("123456789-10");
        jogador.setTelefone("123456789");
        jogador.setLogradouro("teste teste");
        jogador.setNumero(123);
        jogador.setComplemento("teste");
        jogador.setBairro("bairro teste");
        jogador.setCidade("cidade teste");
        jogador.setEstado("uf teste");
        jogador.setCep("1234545");
        jogador.setNacionalidade("teste");
        jogador.setAltura((float)1.80);
        jogador.setPeso((float)80.0);
        jogador.setPosicoes(posicoes);

        return jogador;
    }

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
        Optional<Jogador> jogadorOptional = Optional.ofNullable(jogador1);
        JogadorRepository repository = createMock(JogadorRepository.class);
        expect(repository.findById((long)1)).andReturn(jogadorOptional);
        JogadorService jogadorService = new JogadorService(repository);
        replay(repository);

        assertEquals(jogadorOptional, jogadorService.getJogadorById((long)1));
    }

    @Test
    void deveSalvarJogador(){
        Jogador jogador = createJogadorTest();
        JogadorRepository jogadorRepository = createMock(JogadorRepository.class);
        expect(jogadorRepository.save(jogador)).andReturn(jogador);
        JogadorService jogadorService = new JogadorService(jogadorRepository);
        replay(jogadorRepository);

        assertEquals(jogador, jogadorService.salvar(jogador));
    }

    @Test
    void deveDeletarJogador(){

        Jogador jogador = new Jogador();
        jogador.setId((long)10);

        JogadorRepository jogadorRepository = Mockito.mock(JogadorRepository.class);
        doNothing().when(jogadorRepository).delete(jogador);
        JogadorService jogadorService = new JogadorService(jogadorRepository);
        jogadorService.excluir(jogador);

        Mockito.verify(jogadorRepository).delete(jogador);
    }


    Jogador jogador;
    JogadorRepository jogadorRepository;
    JogadorService jogadorService;

    @BeforeEach
    void setUpValidar(){
        jogador = createJogadorTest();
        jogadorRepository = createMock(JogadorRepository.class);
        jogadorService = new JogadorService(jogadorRepository);
    }

    @Test
    void verificaValidarComJogadorValido(){
        jogadorService.validar(jogador);
    }

    @Test
    void verificaValidarComNomeNull(){
        try {
            jogador.setNome(null);
            jogadorService.validar(jogador);
        } catch (RegraNegocioException e){
            assertEquals("Nome inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComNomeVazio(){
        try {
            jogador.setNome("");
            jogadorService.validar(jogador);
        } catch (RegraNegocioException e){
            assertEquals("Nome inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComEmailNull(){
        try {
            jogador.setEmail(null);
            jogadorService.validar(jogador);
        } catch (RegraNegocioException e){
            assertEquals("E-mail inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComEmailVazio(){
        try {
            jogador.setEmail("");
            jogadorService.validar(jogador);
        } catch (RegraNegocioException e){
            assertEquals("E-mail inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCPFNull(){
        try {
            jogador.setCpf(null);
            jogadorService.validar(jogador);
        } catch (RegraNegocioException e){
            assertEquals("CPF inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCPFVazio(){
        try {
            jogador.setCpf("");
            jogadorService.validar(jogador);
        } catch (RegraNegocioException e){
            assertEquals("CPF inválido", e.getMessage());
        }
    }
}

