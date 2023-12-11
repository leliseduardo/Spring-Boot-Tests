package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Tecnico;
import com.example.scaapi.model.entity.Posicao;
import com.example.scaapi.model.repository.TecnicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.createMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

class TecnicoServiceTest {

    private Tecnico createTecnicoTest(){
        Tecnico tecnico = new Tecnico();
        tecnico.setId((long)1);
        tecnico.setAtivo(true);
        tecnico.setNome("Tecnico teste");
        tecnico.setEmail("email@email.com");
        tecnico.setLogin("login teste");
        tecnico.setDataNascimento(LocalDate.of(1990, 01, 15));
        tecnico.setCpf("123456789-10");
        tecnico.setTelefone("123456789");
        tecnico.setLogradouro("teste teste");
        tecnico.setNumero(123);
        tecnico.setComplemento("teste");
        tecnico.setBairro("bairro teste");
        tecnico.setCidade("cidade teste");
        tecnico.setEstado("uf teste");
        tecnico.setCep("1234545");
        tecnico.setNacionalidade("teste");

        return tecnico;
    }

    @Test
    void deveRetornarGetTecnicoes(){
        Tecnico tecnico1 = new Tecnico();
        tecnico1.setNome("Tecnico1 Teste");
        Tecnico tecnico2 = new Tecnico();
        tecnico2.setNome("Tecnico2 teste");
        List<Tecnico> tecnicoes = new ArrayList<Tecnico>();
        tecnicoes.add(tecnico1);
        tecnicoes.add(tecnico2);
        TecnicoRepository repository = createMock(TecnicoRepository.class);
        expect(repository.findAll()).andReturn(tecnicoes);
        TecnicoService tecnicoService = new TecnicoService(repository);
        replay(repository);

        assertEquals(tecnicoes, tecnicoService.getTecnicos());
    }

    @Test
    void deveRetornarTecnicoComIdUm(){
        Tecnico tecnico1 = new Tecnico();
        Optional<Tecnico> tecnicoOptional = Optional.ofNullable(tecnico1);
        TecnicoRepository repository = createMock(TecnicoRepository.class);
        expect(repository.findById((long)1)).andReturn(tecnicoOptional);
        TecnicoService tecnicoService = new TecnicoService(repository);
        replay(repository);

        assertEquals(tecnicoOptional, tecnicoService.getTecnicoById((long)1));
    }

    @Test
    void deveSalvarTecnico(){
        Tecnico tecnico = createTecnicoTest();
        TecnicoRepository tecnicoRepository = createMock(TecnicoRepository.class);
        expect(tecnicoRepository.save(tecnico)).andReturn(tecnico);
        TecnicoService tecnicoService = new TecnicoService(tecnicoRepository);
        replay(tecnicoRepository);

        assertEquals(tecnico, tecnicoService.salvar(tecnico));
    }

    @Test
    void deveDeletarTecnico(){

        Tecnico tecnico = new Tecnico();
        tecnico.setId((long)10);

        TecnicoRepository tecnicoRepository = Mockito.mock(TecnicoRepository.class);
        doNothing().when(tecnicoRepository).delete(tecnico);
        TecnicoService tecnicoService = new TecnicoService(tecnicoRepository);
        tecnicoService.excluir(tecnico);

        Mockito.verify(tecnicoRepository).delete(tecnico);
    }


    Tecnico tecnico;
    TecnicoRepository tecnicoRepository;
    TecnicoService tecnicoService;

    @BeforeEach
    void setUpValidar(){
        tecnico = createTecnicoTest();
        tecnicoRepository = createMock(TecnicoRepository.class);
        tecnicoService = new TecnicoService(tecnicoRepository);
    }

    @Test
    void verificaValidarComTecnicoValido(){
        tecnicoService.validar(tecnico);
    }

    @Test
    void verificaValidarComNomeNull(){
        try {
            tecnico.setNome(null);
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Nome inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComNomeVazio(){
        try {
            tecnico.setNome("");
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Nome inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComEmailNull(){
        try {
            tecnico.setEmail(null);
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("E-mail inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComEmailVazio(){
        try {
            tecnico.setEmail("");
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("E-mail inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCPFNull(){
        try {
            tecnico.setCpf(null);
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("CPF inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCPFVazio(){
        try {
            tecnico.setCpf("");
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("CPF inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComTelefoneNull(){
        try {
            tecnico.setTelefone(null);
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Telefone inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComTelefoneVazio(){
        try {
            tecnico.setTelefone("");
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Telefone inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComLogradouroNull(){
        try {
            tecnico.setLogradouro(null);
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Logradouro inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComLogradouroVazio(){
        try {
            tecnico.setLogradouro("");
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Logradouro inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComNumeroNull(){
        try {
            tecnico.setNumero(null);
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Número inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComNumeroZero(){
        try {
            tecnico.setNumero(0);
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Número inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComBairroNull(){
        try {
            tecnico.setBairro(null);
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Bairro inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComBairroVazio(){
        try {
            tecnico.setBairro("");
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Bairro inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCidadeNull(){
        try {
            tecnico.setCidade(null);
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Cidade inválida", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCidadeVazia(){
        try {
            tecnico.setCidade("");
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Cidade inválida", e.getMessage());
        }
    }

    @Test
    void verificaValidarComEstadoNull(){
        try {
            tecnico.setEstado(null);
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Estado inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComEstadoVazio(){
        try {
            tecnico.setEstado("");
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Estado inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCepNull(){
        try {
            tecnico.setCep(null);
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("CEP inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCepVazio(){
        try {
            tecnico.setCep("");
            tecnicoService.validar(tecnico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("CEP inválido", e.getMessage());
        }
    }
}