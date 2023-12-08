package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.ScoutPartidaDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.ScoutPartida;
import com.example.scaapi.service.ScoutsPartidaService;
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
@RequestMapping("/api/v1/scouts-partida")
@RequiredArgsConstructor
@Api(tags="API de Scouts da partida")
public class ScoutPartidaController {
    private final ScoutsPartidaService service;

    @GetMapping
    @ApiOperation("Obter todas os Scouts da Partida")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Scouts da Partida encontrados")
    })
    public ResponseEntity get(){
        List<ScoutPartida> scoutPartida = service.getScoutsPartida();
        return ResponseEntity.ok(scoutPartida.stream().map(ScoutPartidaDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de Scouts da Partida")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Scouts da Partida encontrados"),
            @ApiResponse(code = 404, message = "Scouts da Partida não encontrados")
    })
    public ResponseEntity getById(@PathVariable long id){
        Optional<ScoutPartida> scoutsPartida = service.getScoutsPartidaById(id);

        if(!scoutsPartida.isPresent()){
            return new ResponseEntity("ScoutsPartida não encontrada", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(scoutsPartida.map(ScoutPartidaDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar novos Scouts da Partida")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Scouts da Partida salvos com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar Scouts da Partida")
    })
    public ResponseEntity post(@RequestBody ScoutPartidaDto dto){
        try{
            ScoutPartida scoutPartida = converter(dto);
            scoutPartida = service.salvar(scoutPartida);
            return new ResponseEntity(scoutPartida, HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar Scouts da partida")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Scouts da partida editados com sucesso"),
            @ApiResponse(code = 404, message = "Scouts da partida não encontrados"),
            @ApiResponse(code = 400, message = "Erro ao salvar Scouts da partida")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ScoutPartidaDto dto) {
        if(!service.getScoutsPartidaById(id).isPresent()){
            return new ResponseEntity("Scout da partida não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            ScoutPartida scoutPartida = converter(dto);
            scoutPartida.setId(id);
            service.salvar(scoutPartida);
            return ResponseEntity.ok(scoutPartida);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar Scouts da Partida")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Scouts da Partida deletados com sucesso"),
            @ApiResponse(code = 404, message = "Scouts da Partida não encontrados"),
            @ApiResponse(code = 400, message = "Erro ao deletar Scouts da Partida")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ScoutPartida> scoutsPartida = service.getScoutsPartidaById(id);
        if(!scoutsPartida.isPresent()) {
            return new ResponseEntity("Scout da partida não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(scoutsPartida.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private ScoutPartida converter(ScoutPartidaDto dto){
        ModelMapper modelMapper = new ModelMapper();
        ScoutPartida scoutPartida = modelMapper.map(dto, ScoutPartida.class);
        return scoutPartida;
    }
}
