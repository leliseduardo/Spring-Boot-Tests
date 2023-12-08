package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Tecnico;
import com.example.scaapi.model.repository.TecnicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TecnicoService {

    private TecnicoRepository repository;

    public TecnicoService(TecnicoRepository repository) {
        this.repository = repository;
    }

    public List<Tecnico> getTecnicos() {
        return this.repository.findAll();
    }

    public Optional<Tecnico> getTecnicoById(Long id) {
        return this.repository.findById(id);
    }

    @Transactional
    public Tecnico salvar(Tecnico tecnico) {
        validar(tecnico);
        return repository.save(tecnico);
    }

    @Transactional
    public void excluir(Tecnico tecnico) {
        Objects.requireNonNull(tecnico.getId());
        repository.delete(tecnico);
    }

    public void validar(Tecnico tecnico) {
        if (tecnico.getNome() == null || tecnico.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (tecnico.getEmail() == null || tecnico.getEmail().trim().equals("")) {
            throw new RegraNegocioException("E-mail inválido");
        }

        if (tecnico.getCpf() == null || tecnico.getCpf().trim().equals("")) {
            throw new RegraNegocioException("CPF inválido");
        }

        if (tecnico.getTelefone() == null || tecnico.getTelefone().trim().equals("")) {
            throw new RegraNegocioException("Telefone inválido");
        }

        if (tecnico.getLogradouro() == null || tecnico.getLogradouro().trim().equals("")) {
            throw new RegraNegocioException("Logradouro inválido");
        }

        if (tecnico.getNumero() == null || tecnico.getNumero() == 0) {
            throw new RegraNegocioException("Número inválido");
        }

        if (tecnico.getBairro() == null || tecnico.getBairro().trim().equals("")) {
            throw new RegraNegocioException("Bairro inválido");
        }

        if (tecnico.getCidade() == null || tecnico.getCidade().trim().equals("")) {
            throw new RegraNegocioException("Cidade inválida");
        }

        if (tecnico.getEstado() == null || tecnico.getEstado().trim().equals("")) {
            throw new RegraNegocioException("Estado inválido");
        }

        if (tecnico.getCep() == null || tecnico.getCep().trim().equals("")) {
            throw new RegraNegocioException("CEP inválido");
        }
    }
}
