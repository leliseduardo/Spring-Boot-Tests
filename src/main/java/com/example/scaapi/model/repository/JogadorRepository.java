package com.example.scaapi.model.repository;

import com.example.scaapi.model.entity.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogadorRepository extends JpaRepository<Jogador, Long> {
}
