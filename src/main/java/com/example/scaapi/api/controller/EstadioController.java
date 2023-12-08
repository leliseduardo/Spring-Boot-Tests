package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.EstadioDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Estadio;
import com.example.scaapi.service.EstadioService;
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
@RequestMapping("/api/v1/estadios")
@RequiredArgsConstructor
@Api(tags="API de Estádios")
public class EstadioController {
    private final EstadioService service;

    @GetMapping()
    @ApiOperation("Obter todos os estádios")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estádios encontrados")
    })
    public ResponseEntity get() {
        List<Estadio> estadios = service.getEstadio();
        return ResponseEntity.ok(estadios.stream().map(EstadioDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um estádio")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estádio encontrado"),
            @ApiResponse(code = 404, message = "Estádio não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Estadio> estadio = service.getEstadioById(id);

        if (!estadio.isPresent()){
            return new ResponseEntity("Estádio não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(estadio.map(EstadioDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar um novo estádio")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Estádio salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar um estádio")
    })
    public ResponseEntity post(@RequestBody EstadioDto dto) {
        try {
            Estadio estadio = converter(dto);
            estadio = service.salvar(estadio);
            return new ResponseEntity(estadio, HttpStatus.CREATED);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar um estádio")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Estádio editado com sucesso"),
            @ApiResponse(code = 404, message = "Estádio não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao salvar um estádio")
    })
    public ResponseEntity atualizar(@PathVariable("id") long id, @RequestBody EstadioDto dto){
        if(!service.getEstadioById(id).isPresent()){
            return new ResponseEntity("Estadio não encontrado", HttpStatus.NOT_FOUND);
        }
        try{
            Estadio estadio = converter(dto);
            estadio.setId(id);
            service.salvar(estadio);
            return ResponseEntity.ok(estadio);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar um estádio")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Estádio deletado com sucesso"),
            @ApiResponse(code = 404, message = "Estádio não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao deletar um estádio")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id){
        Optional<Estadio> estadio = service.getEstadioById(id);
        if(!estadio.isPresent()){
            return new ResponseEntity("Estadio não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(estadio.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Estadio converter(EstadioDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Estadio estadio = modelMapper.map(dto, Estadio.class);
        return estadio;
    }
}
