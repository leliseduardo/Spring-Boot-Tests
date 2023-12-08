package com.example.scaapi.api.dto;

import com.example.scaapi.model.entity.Local;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalDto {
    private Long id;
    private String nome;

    public static LocalDto create(Local local) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(local, LocalDto.class);
    }
}
