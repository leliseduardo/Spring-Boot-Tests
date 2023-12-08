package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Lesao;
import com.example.scaapi.model.entity.Local;
import com.example.scaapi.model.repository.LesaoRepository;
import com.example.scaapi.model.repository.LocalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LocalService {

    private LocalRepository repository;

    public LocalService(LocalRepository repository){
        this.repository = repository;
    }

    public List<Local> getLocais() {
        return repository.findAll();
    }

    public Optional<Local> getLocalById (Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Local salvar(Local local) {
        validar(local);
        return repository.save(local);
    }

    @Transactional
    public void excluir(Local local) {
        Objects.requireNonNull(local.getId());
        repository.delete(local);
    }

    public void validar(Local local) {
        if (local.getNome() == null || local.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
    }
}
