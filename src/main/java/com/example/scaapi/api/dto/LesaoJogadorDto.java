package com.example.scaapi.api.dto;

import com.example.scaapi.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LesaoJogadorDto {
    private Long id;
    private boolean ativo;
    private LocalDate dataLesao;
    private LocalDate dataDaAlta;
    private Long jogadorId;
    private Long lesaoId;
    private Long localId;
    private Long medicoResponsavelId;

    public static LesaoJogadorDto create(LesaoJogador lesaoJogador) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(lesaoJogador, LesaoJogadorDto.class);
    }
}
