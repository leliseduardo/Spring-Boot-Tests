package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Medico;
import com.example.scaapi.model.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MedicoService {

    private MedicoRepository repository;

    public MedicoService(MedicoRepository repository) {
        this.repository = repository;
    }

    public List<Medico> getMedicos() {
        return this.repository.findAll();
    }

    public Optional<Medico> getMedicoById(Long id) {
        return this.repository.findById(id);
    }

    @Transactional
    public Medico salvar(Medico medico) {
        validar(medico);
        return repository.save(medico);
    }

    @Transactional
    public void excluir(Medico medico) {
        Objects.requireNonNull(medico.getId());
        repository.delete(medico);
    }

    public void validar(Medico medico) {
        if (medico.getNome() == null || medico.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (medico.getEmail() == null || medico.getEmail().trim().equals("")) {
            throw new RegraNegocioException("E-mail inválido");
        }

        if (medico.getCpf() == null || medico.getCpf().trim().equals("")) {
            throw new RegraNegocioException("CPF inválido");
        }

        if (medico.getTelefone() == null || medico.getTelefone().trim().equals("")) {
            throw new RegraNegocioException("Telefone inválido");
        }

        if (medico.getLogradouro() == null || medico.getLogradouro().trim().equals("")) {
            throw new RegraNegocioException("Logradouro inválido");
        }

        if (medico.getNumero() == null || medico.getNumero() == 0) {
            throw new RegraNegocioException("Número inválido");
        }

        if (medico.getBairro() == null || medico.getBairro().trim().equals("")) {
            throw new RegraNegocioException("Bairro inválido");
        }

        if (medico.getCidade() == null || medico.getCidade().trim().equals("")) {
            throw new RegraNegocioException("Cidade inválida");
        }

        if (medico.getEstado() == null || medico.getEstado().trim().equals("")) {
            throw new RegraNegocioException("Estado inválido");
        }

        if (medico.getCep() == null || medico.getCep().trim().equals("")) {
            throw new RegraNegocioException("CEP inválido");
        }

        if (medico.getRegistro() == null || medico.getRegistro().trim().equals("")) {
            throw new RegraNegocioException("Registro inválido");
        }
    }
}
