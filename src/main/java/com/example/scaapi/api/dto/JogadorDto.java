package com.example.scaapi.api.dto;

import com.example.scaapi.model.entity.Jogador;
import com.example.scaapi.model.entity.Posicao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JogadorDto {
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
    private float altura;
    private float peso;
    private List<Long> ids_posicoes;

    public static JogadorDto create(Jogador jogador) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(jogador, JogadorDto.class);
    }

    public JogadorDto createDinamico(Jogador jogador) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(jogador, JogadorDto.class);
    }
}
