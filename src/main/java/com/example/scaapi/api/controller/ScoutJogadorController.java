package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.ScoutJogadorDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.ScoutJogador;
import com.example.scaapi.service.ScoutsJogadorService;
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
@RequestMapping("/api/v1/scouts-jogador")
@RequiredArgsConstructor
@Api(tags="API de Scouts do jogador")
public class ScoutJogadorController {
    private final ScoutsJogadorService service;

    @GetMapping
    @ApiOperation("Obter todas os Scouts de Jogador")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Scouts de Jogador encontrados")
    })
    public ResponseEntity get(){
        List<ScoutJogador> scoutJogador = service.getScoutsJogador();
        return ResponseEntity.ok(scoutJogador.stream().map(ScoutJogadorDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de Scouts de Jogador")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Scouts de jogador encontrados"),
            @ApiResponse(code = 404, message = "Scouts de jogador não encontrados")
    })
    public ResponseEntity getById(@PathVariable long id){
        Optional<ScoutJogador> scoutsJogador = service.getScoutsJogadorById(id);

        if(!scoutsJogador.isPresent()){
            return new ResponseEntity("Scouts do Jogador não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(scoutsJogador.map(ScoutJogadorDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar novos Scouts de Jogador")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Scouts de Jogador salvos com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar Scouts de Jogador")
    })
    public ResponseEntity post(@RequestBody ScoutJogadorDto dto){
        try{
            ScoutJogador scoutJogador = converter(dto);
            scoutJogador = service.salvar(scoutJogador);
            return new ResponseEntity(scoutJogador, HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar Scouts de jogadores")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Scouts de Jogador editados com sucesso"),
            @ApiResponse(code = 404, message = "Scouts de Jogador não encontrados"),
            @ApiResponse(code = 400, message = "Erro ao salvar Scouts de Jogador")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ScoutJogadorDto dto) {
        if(!service.getScoutsJogadorById(id).isPresent()){
            return new ResponseEntity("Scout do jogador não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            ScoutJogador scoutJogador = converter(dto);
            scoutJogador.setId(id);
            service.salvar(scoutJogador);
            return ResponseEntity.ok(scoutJogador);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar Scouts de Jogador")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Scouts de Jogador deletados com sucesso"),
            @ApiResponse(code = 404, message = "Scouts de Jogador não encontrados"),
            @ApiResponse(code = 400, message = "Erro ao deletar Scouts de Jogador")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ScoutJogador> scoutsJogador = service.getScoutsJogadorById(id);
        if(!scoutsJogador.isPresent()) {
            return new ResponseEntity("Scout do jogador não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(scoutsJogador.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private ScoutJogador converter(ScoutJogadorDto dto){
        ModelMapper modelMapper = new ModelMapper();
        ScoutJogador scoutJogador = modelMapper.map(dto, ScoutJogador.class);
        return scoutJogador;
    }
}
