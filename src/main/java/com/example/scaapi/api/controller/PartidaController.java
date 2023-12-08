package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.MedicoDto;
import com.example.scaapi.api.dto.PartidaDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Medico;
import com.example.scaapi.model.entity.Partida;
import com.example.scaapi.service.PartidaService;
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
@RequestMapping("/api/v1/partidas")
@RequiredArgsConstructor
@Api(tags="API de Partidas")
public class PartidaController {
    private final PartidaService service;

    @GetMapping()
    @ApiOperation("Obter todos as partidas")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Partidas encontradas")
    })
    public ResponseEntity get() {
        List<Partida> partidas = service.getPartida();
        return ResponseEntity.ok(partidas.stream().map(PartidaDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma partida")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Partida encontrada"),
            @ApiResponse(code = 404, message = "Partida não encontrada")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Partida> partida = service.getPartidaById(id);

        if (!partida.isPresent()) {
            return new ResponseEntity("Partida não encontrada", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(partida.map(PartidaDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar uma nova partida")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Partida salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar uma partida")
    })
    public ResponseEntity post(@RequestBody PartidaDto dto) {
        try {
            Partida partida = converter(dto);
            partida = service.salvar(partida);
            return new ResponseEntity(partida, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    @ApiOperation("Atualizar uma partida")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Partida salva com sucesso"),
            @ApiResponse(code = 404, message = "Partida não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao salvar a partida")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody PartidaDto dto){
        if(!service.getPartidaById(id).isPresent()){
            return new ResponseEntity("Partida não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Partida partida = converter(dto);
            partida.setId(id);
            service.salvar(partida);
            return ResponseEntity.ok(partida);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Excluir uma partida")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Partida deletada com sucesso"),
            @ApiResponse(code = 404, message = "Partida não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao deletar uma partida")
    })
    public ResponseEntity excluir(@PathVariable("id") long id) {
        Optional<Partida> partida = service.getPartidaById(id);
        if (!partida.isPresent()) {
            return new ResponseEntity<>("Partida não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(partida.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    public Partida converter(PartidaDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Partida partida = modelMapper.map(dto, Partida.class);
        return partida;
    }

}
