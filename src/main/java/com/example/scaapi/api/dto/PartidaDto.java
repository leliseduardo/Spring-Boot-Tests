package com.example.scaapi.api.dto;

import com.example.scaapi.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartidaDto {
    private Long id;
    private LocalDate data;
    private LocalDateTime horario;
    private Long timeAdversarioId;
    private Long campeonatoId;
    private Long estadioId;
    private Long tecnicoId;

    public static PartidaDto create(Partida partida) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(partida, PartidaDto.class);
    }
}
