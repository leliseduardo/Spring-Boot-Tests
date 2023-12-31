package com.example.scaapi.model.entity;

import com.example.scaapi.api.dto.JogadorDto;
import com.example.scaapi.model.repository.PosicaoRepository;
import com.example.scaapi.service.PosicaoService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jogador extends Pessoa{

    private Float altura;
    private Float peso;
    @ManyToMany
    @JoinTable(
            name = "posicao_jogador",
            joinColumns = @JoinColumn(name = "jogador_id"),
            inverseJoinColumns = @JoinColumn(name = "posicao_id")
    )
    private List<Posicao> posicoes = new ArrayList<Posicao>();

    public List<Long> getIdPosicoes(){
        return posicoes.stream().map(posicao -> { return posicao.getId();}).collect(Collectors.toList());
    }
}
