package com.example.scaapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate data;
    private LocalDateTime horario;
    @ManyToOne
    private TimeAdversario timeAdversario;
    @ManyToOne
    private Campeonato campeonato;
    @ManyToOne
    private Estadio estadio;
    @ManyToOne
    private Tecnico tecnico;
}
