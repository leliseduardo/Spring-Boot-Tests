package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.TecnicoDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Tecnico;
import com.example.scaapi.service.TecnicoService;
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
@RequestMapping("/api/v1/tecnicos")
@RequiredArgsConstructor
@Api(tags="API de Tecnicos")
public class TecnicoController {
    private final TecnicoService service;

    @GetMapping
    @ApiOperation("Obter todos os Técnicos")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Técnicos encontrados")
    })
    public ResponseEntity get(){
        List<Tecnico> tecnicos = service.getTecnicos();
        return ResponseEntity.ok(tecnicos.stream().map(TecnicoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes do Técnico")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Técnico encontrado"),
            @ApiResponse(code = 404, message = "Técnico não encontrado")
    })
    public ResponseEntity getById(@PathVariable long id){
        Optional<Tecnico> tecnico = service.getTecnicoById(id);

        if(!tecnico.isPresent()){
            return new ResponseEntity("Tecnico não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(tecnico.map(TecnicoDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar novo Técnico")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Técnico salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar Técnico")
    })
    public ResponseEntity post(@RequestBody TecnicoDto dto){
        try{
            Tecnico tecnico = converter(dto);
            tecnico = service.salvar(tecnico);
            return new ResponseEntity(tecnico, HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar Técnico")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Técnico editado com sucesso"),
            @ApiResponse(code = 404, message = "Técnico não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao salvar Técnico")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody TecnicoDto dto) {
        if(!service.getTecnicoById(id).isPresent()){
            return new ResponseEntity("Tecnico não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Tecnico tecnico = converter(dto);
            tecnico.setId(id);
            service.salvar(tecnico);
            return ResponseEntity.ok(tecnico);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar Técnico")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Técnico deletado com sucesso"),
            @ApiResponse(code = 404, message = "Técnico não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao deletar Técnico")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Tecnico> tecnico = service.getTecnicoById(id);
        if(!tecnico.isPresent()) {
            return new ResponseEntity("Tecnico não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(tecnico.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Tecnico converter(TecnicoDto dto){
        ModelMapper modelMapper = new ModelMapper();
        Tecnico tecnico = modelMapper.map(dto, Tecnico.class);
        return tecnico;
    }
}