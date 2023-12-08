package com.example.scaapi.api.dto;

import com.example.scaapi.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoutPartidaDto {
    private Long id;
    private Float posseDeBola;
    private Integer impedimentos;
    private Integer cruzamentos;
    private Integer escanteios;
    private Integer gols;
    private Integer golsTimeAdversario;
    private Long partidaId;

    public static ScoutPartidaDto create(ScoutPartida scoutPartida) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(scoutPartida, ScoutPartidaDto.class);
    }
}
