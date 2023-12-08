package com.example.scaapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoutJogador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @PositiveOrZero
    private int desarmes;
    @PositiveOrZero
    private int faltasCometidas;
    @PositiveOrZero
    private int golContra;
    @PositiveOrZero
    private int cartaoAmarelo;
    @NotNull
    private boolean cartaoVermelho;
    @PositiveOrZero
    private int faltaSofrida;
    @PositiveOrZero
    private int passeIncompleto;
    @PositiveOrZero
    private int assistencia;
    @PositiveOrZero
    private int finalizacaoTrave;
    @PositiveOrZero
    private int finalizacaoDefendida;
    @PositiveOrZero
    private int finalizacaoFora;
    @PositiveOrZero
    private int gol;
    @PositiveOrZero
    private int impedimento;
    @PositiveOrZero
    private int penaltiPerdido;
    @PositiveOrZero
    private int penaltiSofrido;
    @PositiveOrZero
    private int penaltiCometido;
    @PositiveOrZero
    private int jogosSemSofrerGols;
    @PositiveOrZero
    private int defesaDificil;
    @PositiveOrZero
    private int defesaPenalti;
    @PositiveOrZero
    private int golSofrido;
    @NotNull
    @OneToOne
    private Relacionado relacaoPartidaJogador;



    // getters and setters
}
