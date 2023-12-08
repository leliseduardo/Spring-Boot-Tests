package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.LesaoDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Lesao;
import com.example.scaapi.service.LesaoService;
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
@RequestMapping("/api/v1/lesoes")
@RequiredArgsConstructor
@Api(tags="API de Lesoes")
public class LesaoController {
    private final LesaoService service;

    @GetMapping()
    @ApiOperation("Obter todos as lesões")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lesões encontradas")
    })
    public ResponseEntity get() {
        List<Lesao> lesoes = service.getLesoes();
        return ResponseEntity.ok(lesoes.stream().map(LesaoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um lesão")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lesão encontrada"),
            @ApiResponse(code = 404, message = "Lesão não encontrada")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Lesao> lesao = service.getLesaoById(id);

        if (!lesao.isPresent()){
            return new ResponseEntity("Lesão não encontrada", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(lesao.map(LesaoDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar uma nova lesão")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Lesão salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar um lesão")
    })
    public ResponseEntity post(@RequestBody LesaoDto dto) {
        try {
            Lesao lesao = converter(dto);
            lesao = service.salvar(lesao);
            return new ResponseEntity(lesao, HttpStatus.CREATED);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar uma nova lesão")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lesão editada com sucesso"),
            @ApiResponse(code = 404, message = "Lesão não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao salvar um lesão")
    })
    public ResponseEntity atualizar(@PathVariable("id") long id, @RequestBody LesaoDto dto){
        if(!service.getLesaoById(id).isPresent()){
            return new ResponseEntity("Lesao não encontrado", HttpStatus.NOT_FOUND);
        }
        try{
            Lesao lesao = converter(dto);
            lesao.setId(id);
            service.salvar(lesao);
            return ResponseEntity.ok(lesao);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar uma lesão")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Lesão deletada com sucesso"),
            @ApiResponse(code = 404, message = "Lesão não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao deletar uma lesão")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id){
        Optional<Lesao> lesao = service.getLesaoById(id);
        if(!lesao.isPresent()){
            return new ResponseEntity("Lesao não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(lesao.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Lesao converter(LesaoDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Lesao lesao = modelMapper.map(dto, Lesao.class);
        return lesao;
    }
}
