package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.TemporadaDto;
import com.example.scaapi.api.dto.TimeAdversarioDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Temporada;
import com.example.scaapi.model.entity.TimeAdversario;
import com.example.scaapi.service.TemporadaService;
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
@RequestMapping("/api/v1/temporadas")
@RequiredArgsConstructor
@Api(tags="API de Temporadas")
public class TemporadaController {
    private final TemporadaService service;

    @GetMapping
    @ApiOperation("Obter todos as Temporadas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Temporadas encontrados")
    })
    public ResponseEntity get(){
        List<Temporada> temporadas = service.getTemporada();
        return ResponseEntity.ok(temporadas.stream().map(TemporadaDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes da Temporada")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Temporada encontrada"),
            @ApiResponse(code = 404, message = "Temporada não encontrada")
    })
    public ResponseEntity getById(@PathVariable long id){
        Optional<Temporada> temporada = service.getTemporadaById(id);

        if(!temporada.isPresent()){
            return new ResponseEntity("Temporada não encontrada", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(temporada.map(TemporadaDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar nova Temporada")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Temporada salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar Temporada")
    })
    public ResponseEntity post(@RequestBody TemporadaDto dto){
        try{
            Temporada temporada = converter(dto);
            temporada = service.salvar(temporada);
            return new ResponseEntity(temporada, HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar Temporada")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Temporada editada com sucesso"),
            @ApiResponse(code = 404, message = "Temporada não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao salvar Temporada")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody TemporadaDto dto) {
        if(!service.getTemporadaById(id).isPresent()){
            return new ResponseEntity("Temporada não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Temporada temporada = converter(dto);
            temporada.setId(id);
            service.salvar(temporada);
            return ResponseEntity.ok(temporada);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar Temporada")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Temporada deletada com sucesso"),
            @ApiResponse(code = 404, message = "Temporada não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao deletar Temporada")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Temporada> temporada = service.getTemporadaById(id);
        if(!temporada.isPresent()) {
            return new ResponseEntity("Temporada não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(temporada.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Temporada converter(TemporadaDto dto){
        ModelMapper modelMapper = new ModelMapper();
        Temporada temporada = modelMapper.map(dto, Temporada.class);
        return temporada;
    }
}