package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Jogador;
import com.example.scaapi.model.repository.JogadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class JogadorService {

    private JogadorRepository repository;

    public JogadorService(JogadorRepository repository) {
        this.repository = repository;
    }

    public List<Jogador> getJogadores() {
        return this.repository.findAll();
    }

    public Optional<Jogador> getJogadorById(Long id) {
        return this.repository.findById(id);
    }

    @Transactional
    public Jogador salvar(Jogador jogador) {
        validar(jogador);
        return repository.save(jogador);
    }

    @Transactional
    public void excluir(Jogador jogador) {
        Objects.requireNonNull(jogador.getId());
        repository.delete(jogador);
    }

    public void validar(Jogador jogador) {
        if (jogador.getNome() == null || jogador.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }

        if (jogador.getEmail() == null || jogador.getEmail().trim().equals("")) {
            throw new RegraNegocioException("E-mail inválido");
        }

        if (jogador.getCpf() == null || jogador.getCpf().trim().equals("")) {
            throw new RegraNegocioException("CPF inválido");
        }

        if (jogador.getTelefone() == null || jogador.getTelefone().trim().equals("")) {
            throw new RegraNegocioException("Telefone inválido");
        }

        if (jogador.getLogradouro() == null || jogador.getLogradouro().trim().equals("")) {
            throw new RegraNegocioException("Logradouro inválido");
        }

        if (jogador.getNumero() == null || jogador.getNumero() == 0) {
            throw new RegraNegocioException("Número inválido");
        }

        if (jogador.getBairro() == null || jogador.getBairro().trim().equals("")) {
            throw new RegraNegocioException("Bairro inválido");
        }

        if (jogador.getCidade() == null || jogador.getCidade().trim().equals("")) {
            throw new RegraNegocioException("Cidade inválida");
        }

        if (jogador.getEstado() == null || jogador.getEstado().trim().equals("")) {
            throw new RegraNegocioException("Estado inválido");
        }

        if (jogador.getCep() == null || jogador.getCep().trim().equals("")) {
            throw new RegraNegocioException("CEP inválido");
        }

        if (jogador.getNacionalidade() == null || jogador.getNacionalidade().trim().equals("")) {
            throw new RegraNegocioException("Nacionalidade inválida");
        }

        if (jogador.getAltura() == null || jogador.getAltura() == 0) {
            throw new RegraNegocioException("Altura inválida");
        }

        if (jogador.getPeso() == null || jogador.getPeso() == 0) {
            throw new RegraNegocioException("Peso inválido");
        }
    }
}
