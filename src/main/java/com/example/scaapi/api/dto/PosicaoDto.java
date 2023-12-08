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
public class PosicaoDto {
    private Long id;
    private String nome;
    private String areaCampo;
    private String ladoCampo;
    private String sigla;

    public static PosicaoDto create(Posicao posicao) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(posicao, PosicaoDto.class);
    }
}
