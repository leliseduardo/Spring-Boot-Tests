package com.example.scaapi.api.dto;

import com.example.scaapi.model.entity.TimeAdversario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeAdversarioDto {
    private Long id;
    private String nome;
    private String photoPath;

    public static TimeAdversarioDto create(TimeAdversario timeAdversario) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(timeAdversario, TimeAdversarioDto.class);
    }
}
