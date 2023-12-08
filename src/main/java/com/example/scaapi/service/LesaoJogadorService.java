package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Lesao;
import com.example.scaapi.model.entity.LesaoJogador;
import com.example.scaapi.model.repository.LesaoJogadorRepository;
import com.example.scaapi.model.repository.LesaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LesaoJogadorService {

    private LesaoJogadorRepository repository;

    public LesaoJogadorService(LesaoJogadorRepository repository){
        this.repository = repository;
    }

    public List<LesaoJogador> getLesoesDoJogador() {
        return repository.findAll();
    }

    public Optional<LesaoJogador> getLesaoJogadorById (Long id) {
        return repository.findById(id);
    }

    @Transactional
    public LesaoJogador salvar(LesaoJogador lesaoJogador) {
        validar(lesaoJogador);
        return repository.save(lesaoJogador);
    }

    @Transactional
    public void excluir(LesaoJogador lesaoJogador) {
        Objects.requireNonNull(lesaoJogador.getId());
        repository.delete(lesaoJogador);
    }

    public void validar(LesaoJogador lesaoJogador) {
        if (lesaoJogador.getJogador() == null || lesaoJogador.getJogador().getId() == null || lesaoJogador.getJogador().getId() == 0) {
            throw new RegraNegocioException("Jogador inválido");
        }

        if (lesaoJogador.getLesao() == null || lesaoJogador.getLesao().getId() == null || lesaoJogador.getLesao().getId() == 0) {
            throw new RegraNegocioException("Lesão inválida");
        }

        if (lesaoJogador.getLocal() == null || lesaoJogador.getLocal().getId() == null || lesaoJogador.getLocal().getId() == 0) {
            throw new RegraNegocioException("Local inválido");
        }
    }
}
