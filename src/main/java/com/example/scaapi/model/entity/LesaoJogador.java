package com.example.scaapi.model.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LesaoJogador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "boolean default true")
    private boolean ativo;
    private LocalDate dataLesao;
    private LocalDate dataDaAlta;
    @ManyToOne
    private Jogador jogador;
    @ManyToOne
    private Lesao lesao;
    @ManyToOne
    private Local local;
    @ManyToOne
    private Medico medicoResponsavel;
}
