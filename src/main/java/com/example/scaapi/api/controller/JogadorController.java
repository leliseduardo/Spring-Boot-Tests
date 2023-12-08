package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.JogadorDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Jogador;
import com.example.scaapi.model.entity.Posicao;
import com.example.scaapi.service.JogadorService;
import com.example.scaapi.service.PosicaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/jogadores")
@RequiredArgsConstructor
@Api(tags="API de Jogadores")
public class JogadorController {
    private final JogadorService service;
    private final PosicaoService posicaoService;

    @GetMapping()
    @ApiOperation("Obter todos os jogadores")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogadores encontrados")
    })
    public ResponseEntity get() {
        List<Jogador> jogadores = service.getJogadores();

        List<JogadorDto> dtos = jogadores.stream().map(jogador -> {
            JogadorDto dto = JogadorDto.create(jogador);
            dto.setIds_posicoes(jogador.getIdPosicoes());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um jogador")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogador encontrado"),
            @ApiResponse(code = 404, message = "Jogador não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Jogador> jogador = service.getJogadorById(id);

        if (!jogador.isPresent()){
            return new ResponseEntity("Jogador não encontrado", HttpStatus.NOT_FOUND);
        }

        JogadorDto dto = JogadorDto.create(jogador.get());
        dto.setIds_posicoes(jogador.get().getIdPosicoes());

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/posicoes")
    @ApiOperation("Obter lista de posições de um jogador")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lista de posicoes encontrada"),
            @ApiResponse(code = 404, message = "Lista de posicoes não encontradas")
    })
    public ResponseEntity getPosicao(@PathVariable("id") Long id) {
        Optional<Jogador> jogador = service.getJogadorById(id);

        if (!jogador.isPresent()){
            return new ResponseEntity("Jogador não encontrado", HttpStatus.NOT_FOUND);
        }

        List<String> posicoes = jogador.get().getPosicoes().stream().map(
                posicao -> {return posicao.getNome();
                }).collect(Collectors.toList());

        return ResponseEntity.ok(posicoes);
    }

    @PostMapping()
    @ApiOperation("Salvar um novo jogador")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Jogador salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar um jogador")
    })
    public ResponseEntity post(@RequestBody JogadorDto dto) {
        try {
            Jogador jogador = converter(dto);
            for(Long id : dto.getIds_posicoes()){
                Posicao posicao = posicaoService.getPosicaoExistenteById(id);
                posicao.getJogadores().add(jogador);
                jogador.getPosicoes().add(posicao);
            }
            jogador = service.salvar(jogador);
            return new ResponseEntity(jogador, HttpStatus.CREATED);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar um jogador")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogador editado com sucesso"),
            @ApiResponse(code = 404, message = "Jogador não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao salvar um jogador")
    })
    public ResponseEntity atualizar(@PathVariable("id") long id, @RequestBody JogadorDto dto){
        if(!service.getJogadorById(id).isPresent()){
            return new ResponseEntity("Jogador não encontrado", HttpStatus.NOT_FOUND);
        }
        try{
            Jogador jogador = converter(dto);
            for(Long ids : dto.getIds_posicoes()){
                Posicao posicao = posicaoService.getPosicaoExistenteById(ids);
                posicao.getJogadores().add(jogador);
                jogador.getPosicoes().add(posicao);
            }
            jogador.setId(id);
            dto = JogadorDto.create(jogador);
            dto.setIds_posicoes(jogador.getIdPosicoes());
            service.salvar(jogador);
            return ResponseEntity.ok(dto);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar um jogador")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Jogador deletado com sucesso"),
            @ApiResponse(code = 404, message = "Jogador não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao deletar um jogador")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id){
        Optional<Jogador> jogador = service.getJogadorById(id);
        if(!jogador.isPresent()){
            return new ResponseEntity("Jogador não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(jogador.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Jogador converter(JogadorDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Jogador jogador = modelMapper.map(dto, Jogador.class);
        return jogador;
    }
}
