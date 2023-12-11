package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Medico;
import com.example.scaapi.model.entity.Posicao;
import com.example.scaapi.model.repository.MedicoRepository;
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

class MedicoServiceTest {

    private Medico createMedicoTest(){
        Medico medico = new Medico();
        medico.setId((long)1);
        medico.setAtivo(true);
        medico.setNome("Medico teste");
        medico.setEmail("email@email.com");
        medico.setLogin("login teste");
        medico.setDataNascimento(LocalDate.of(1990, 01, 15));
        medico.setCpf("123456789-10");
        medico.setTelefone("123456789");
        medico.setLogradouro("teste teste");
        medico.setNumero(123);
        medico.setComplemento("teste");
        medico.setBairro("bairro teste");
        medico.setCidade("cidade teste");
        medico.setEstado("uf teste");
        medico.setCep("1234545");
        medico.setNacionalidade("teste");
        medico.setRegistro("12345678");

        return medico;
    }

    @Test
    void deveRetornarGetMedicos(){
        Medico medico1 = new Medico();
        medico1.setNome("Medico1 Teste");
        Medico medico2 = new Medico();
        medico2.setNome("Medico2 teste");
        List<Medico> medicoes = new ArrayList<Medico>();
        medicoes.add(medico1);
        medicoes.add(medico2);
        MedicoRepository repository = createMock(MedicoRepository.class);
        expect(repository.findAll()).andReturn(medicoes);
        MedicoService medicoService = new MedicoService(repository);
        replay(repository);

        assertEquals(medicoes, medicoService.getMedicos());
    }

    @Test
    void deveRetornarMedicoComIdUm(){
        Medico medico1 = new Medico();
        Optional<Medico> medicoOptional = Optional.ofNullable(medico1);
        MedicoRepository repository = createMock(MedicoRepository.class);
        expect(repository.findById((long)1)).andReturn(medicoOptional);
        MedicoService medicoService = new MedicoService(repository);
        replay(repository);

        assertEquals(medicoOptional, medicoService.getMedicoById((long)1));
    }

    @Test
    void deveSalvarMedico(){
        Medico medico = createMedicoTest();
        MedicoRepository medicoRepository = createMock(MedicoRepository.class);
        expect(medicoRepository.save(medico)).andReturn(medico);
        MedicoService medicoService = new MedicoService(medicoRepository);
        replay(medicoRepository);

        assertEquals(medico, medicoService.salvar(medico));
    }

    @Test
    void deveDeletarMedico(){

        Medico medico = new Medico();
        medico.setId((long)10);

        MedicoRepository medicoRepository = Mockito.mock(MedicoRepository.class);
        doNothing().when(medicoRepository).delete(medico);
        MedicoService medicoService = new MedicoService(medicoRepository);
        medicoService.excluir(medico);

        Mockito.verify(medicoRepository).delete(medico);
    }


    Medico medico;
    MedicoRepository medicoRepository;
    MedicoService medicoService;

    @BeforeEach
    void setUpValidar(){
        medico = createMedicoTest();
        medicoRepository = createMock(MedicoRepository.class);
        medicoService = new MedicoService(medicoRepository);
    }

    @Test
    void verificaValidarComMedicoValido(){
        medicoService.validar(medico);
    }

    @Test
    void verificaValidarComNomeNull(){
        try {
            medico.setNome(null);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Nome inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComNomeVazio(){
        try {
            medico.setNome("");
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Nome inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComEmailNull(){
        try {
            medico.setEmail(null);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("E-mail inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComEmailVazio(){
        try {
            medico.setEmail("");
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("E-mail inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCPFNull(){
        try {
            medico.setCpf(null);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("CPF inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCPFVazio(){
        try {
            medico.setCpf("");
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("CPF inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComTelefoneNull(){
        try {
            medico.setTelefone(null);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Telefone inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComTelefoneVazio(){
        try {
            medico.setTelefone("");
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Telefone inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComLogradouroNull(){
        try {
            medico.setLogradouro(null);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Logradouro inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComLogradouroVazio(){
        try {
            medico.setLogradouro("");
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Logradouro inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComNumeroNull(){
        try {
            medico.setNumero(null);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Número inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComNumeroZero(){
        try {
            medico.setNumero(0);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Número inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComBairroNull(){
        try {
            medico.setBairro(null);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Bairro inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComBairroVazio(){
        try {
            medico.setBairro("");
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Bairro inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCidadeNull(){
        try {
            medico.setCidade(null);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Cidade inválida", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCidadeVazia(){
        try {
            medico.setCidade("");
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Cidade inválida", e.getMessage());
        }
    }

    @Test
    void verificaValidarComEstadoNull(){
        try {
            medico.setEstado(null);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Estado inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComEstadoVazio(){
        try {
            medico.setEstado("");
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Estado inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCepNull(){
        try {
            medico.setCep(null);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("CEP inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComCepVazio(){
        try {
            medico.setCep("");
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("CEP inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComRegistroNull(){
        try {
            medico.setRegistro(null);
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Registro inválido", e.getMessage());
        }
    }

    @Test
    void verificaValidarComRegistroVazio(){
        try {
            medico.setRegistro("");
            medicoService.validar(medico);
            fail();
        } catch (RegraNegocioException e){
            assertEquals("Registro inválido", e.getMessage());
        }
    }
}