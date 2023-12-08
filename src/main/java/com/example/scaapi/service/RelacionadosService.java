package com.example.scaapi.service;

import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Relacionado;
import com.example.scaapi.model.repository.RelacionadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RelacionadosService {

    private RelacionadoRepository repository;

    public RelacionadosService(RelacionadoRepository repository) {
        this.repository = repository;
    }

    public List<Relacionado> getRelacionados() {
        return this.repository.findAll();
    }

    public Optional<Relacionado> getRelacionadosById(Long id) {
        return this.repository.findById(id);
    }

    public List<Relacionado> getRelacionadosByPartidaId(Long partidaId) {
        return this.repository.findByPartidaId(partidaId);
    }

    @Transactional
    public Relacionado salvar(Relacionado relacionado) {
        validar(relacionado);
        return repository.save(relacionado);
    }

    @Transactional
    public void excluir(Relacionado relacionado) {
        Objects.requireNonNull(relacionado.getId());
        repository.delete(relacionado);
    }

    public void validar(Relacionado relacionado) {
        if (relacionado.getJogador() == null || relacionado.getJogador().getId() == null || relacionado.getJogador().getId() == 0) {
            throw new RegraNegocioException("Jogador inválido");
        }

        if (relacionado.getPartida() == null || relacionado.getPartida().getId() == null || relacionado.getPartida().getId() == 0) {
            throw new RegraNegocioException("Partida inválida");
        }
    }
}
