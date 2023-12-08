package com.example.scaapi.api.dto;

import com.example.scaapi.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoutJogadorDto {
    private Long id;
    private int desarmes;
    private int faltasCometidas;
    private int golContra;
    private int cartaoAmarelo;
    private boolean cartaoVermelho;
    private int faltaSofrida;
    private int passeIncompleto;
    private int assistencia;
    private int finalizacaoTrave;
    private int finalizacaoDefendida;
    private int finalizacaoFora;
    private int gol;
    private int impedimento;
    private int penaltiPerdido;
    private int penaltiSofrido;
    private int penaltiCometido;
    private int jogosSemSofrerGols;
    private int defesaDificil;
    private int defesaPenalti;
    private int golSofrido;
    private Long relacaoPartidaJogadorId;

    public static ScoutJogadorDto create(ScoutJogador scoutJogador) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(scoutJogador, ScoutJogadorDto.class);
    }
}
