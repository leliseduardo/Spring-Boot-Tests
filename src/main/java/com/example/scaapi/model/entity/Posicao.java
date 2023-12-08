package com.example.scaapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Posicao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String areaCampo;
    private String ladoCampo;
    private String sigla;
    @ManyToMany(mappedBy = "posicoes")
    private List<Jogador> jogadores = new ArrayList<Jogador>();

}
