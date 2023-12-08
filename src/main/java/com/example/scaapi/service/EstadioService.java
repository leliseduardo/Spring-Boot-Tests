package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Estadio;
import com.example.scaapi.model.repository.EstadioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EstadioService {
    private EstadioRepository repository;

    public EstadioService(EstadioRepository repository) {
        this.repository = repository;
    }

    public List<Estadio> getEstadio() {
        return this.repository.findAll();
    }

    public Optional<Estadio> getEstadioById(Long id) {
        return this.repository.findById(id);
    }

    @Transactional
    public Estadio salvar(Estadio estadio) {
        validar(estadio);
        return repository.save(estadio);
    }

    @Transactional
    public void excluir(Estadio estadio) {
        Objects.requireNonNull(estadio.getId());
        repository.delete(estadio);
    }

    public void validar(Estadio estadio) {
        if (estadio.getNome() == null || estadio.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (estadio.getLogradouro() == null || estadio.getLogradouro().trim().equals("")) {
            throw new RegraNegocioException("Logradouro inválido");
        }

        if (estadio.getNumero() == null || estadio.getNumero() == 0) {
            throw new RegraNegocioException("Número inválido");
        }

        if (estadio.getBairro() == null || estadio.getBairro().trim().equals("")) {
            throw new RegraNegocioException("Bairro inválido");
        }

        //if (jogador.getComplemento() == null || jogador.getComplemento().trim().equals("")) {
        // throw new RegraNegocioException("Complemento inválido");
        //}

        if (estadio.getCidade() == null || estadio.getCidade().trim().equals("")) {
            throw new RegraNegocioException("Cidade inválida");
        }

        if (estadio.getUf() == null || estadio.getUf().trim().equals("")) {
            throw new RegraNegocioException("UF inválido");
        }

        if (estadio.getCep() == null || estadio.getCep().trim().equals("")) {
            throw new RegraNegocioException("CEP inválido");
        }

    }
}