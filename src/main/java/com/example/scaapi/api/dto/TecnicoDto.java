package com.example.scaapi.api.dto;

import com.example.scaapi.model.entity.Jogador;
import com.example.scaapi.model.entity.Tecnico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnicoDto {
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
    private LocalDate dataDeContratacao;
    private LocalDate dataDeDemissao;

    public static TecnicoDto create(Tecnico tecnico) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(tecnico, TecnicoDto.class);
    }
}
