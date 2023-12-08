package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.LesaoJogador;
import com.example.scaapi.model.entity.Posicao;
import com.example.scaapi.model.repository.PosicaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PosicaoService {

    private PosicaoRepository repository;

    public PosicaoService (PosicaoRepository repository) {
        this.repository = repository;
    }

    public List<Posicao> getPosicoes() {
        return repository.findAll();
    }

    public Optional<Posicao> getPosicaoById (Long id) {
        return repository.findById(id);
    }

    public Posicao getPosicaoExistenteById (Long id) {
        return repository.getById(id);
    }

    @Transactional
    public Posicao salvar(Posicao posicao) {
        validar(posicao);
        return repository.save(posicao);
    }

    @Transactional
    public void excluir(Posicao posicao) {
        Objects.requireNonNull(posicao.getId());
        repository.delete(posicao);
    }

    public void validar(Posicao posicao) {
        if (posicao.getNome() == null || posicao.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (posicao.getAreaCampo() == null || posicao.getAreaCampo().trim().equals("")) {
            throw new RegraNegocioException("Area do Campo inválida");
        }

        if (posicao.getLadoCampo() == null || posicao.getLadoCampo().trim().equals("")) {
            throw new RegraNegocioException("Lado do Campo inválido");
        }

        if (posicao.getSigla() == null || posicao.getSigla().trim().equals("")) {
            throw new RegraNegocioException("Sigla inválida");
        }
    }
}
