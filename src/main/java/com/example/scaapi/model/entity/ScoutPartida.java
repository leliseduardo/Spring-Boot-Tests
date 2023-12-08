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
public class ScoutPartida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @PositiveOrZero
    private float posseDeBola;
    @PositiveOrZero
    private int impedimentos;
    @PositiveOrZero
    private int cruzamentos;
    @PositiveOrZero
    private int escanteios;
    @PositiveOrZero
    private Integer gols;
    @PositiveOrZero
    private Integer golsTimeAdversario;
    @NotNull
    @OneToOne
    private Partida partida;
}
