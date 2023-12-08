package com.example.scaapi.api.dto;

import com.example.scaapi.model.entity.Jogador;
import com.example.scaapi.model.entity.Tecnico;
import com.example.scaapi.model.entity.Temporada;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemporadaDto {
    private Long id;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public static TemporadaDto create(Temporada temporada) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(temporada, TemporadaDto.class);
    }
}
