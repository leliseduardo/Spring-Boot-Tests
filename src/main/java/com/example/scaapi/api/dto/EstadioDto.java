package com.example.scaapi.api.dto;

import com.example.scaapi.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadioDto {
    private Long id;
    private String nome;
    private String logradouro;
    private Integer numero;
    private String bairro;
    private String cep;
    private String cidade;
    private String uf;

    public static EstadioDto create(Estadio estadio) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(estadio, EstadioDto.class);
    }
}
