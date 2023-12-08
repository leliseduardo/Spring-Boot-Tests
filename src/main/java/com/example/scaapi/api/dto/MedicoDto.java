package com.example.scaapi.api.dto;

import com.example.scaapi.model.entity.Jogador;
import com.example.scaapi.model.entity.Medico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoDto {
    private Long id;
    private boolean ativo;
    private String nome;
    private String email;
    private String login;
    private LocalDate dataNascimento;
    private String cpf;
    private String telefone;
    private String logradouro;
    private Integer numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String pais;
    private String cep;
    private String nacionalidade;
    private String registro;

    public static MedicoDto create(Medico medico) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(medico, MedicoDto.class);
    }
}
