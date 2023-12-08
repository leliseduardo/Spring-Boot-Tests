package com.example.scaapi.model.repository;


import com.example.scaapi.model.entity.Posicao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosicaoRepository extends JpaRepository<Posicao, Long> {
}
