package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.LocalDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Local;
import com.example.scaapi.service.LocalService;
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
@RequestMapping("/api/v1/locais")
@RequiredArgsConstructor
@Api(tags="API de Locais")
public class LocalController {
    private final LocalService service;

    @GetMapping()
    @ApiOperation("Obter todos os locais")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Locais encontrados")
    })
    public ResponseEntity get() {
        List<Local> local = service.getLocais();
        return ResponseEntity.ok(local.stream().map(LocalDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um local")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Local encontrado"),
            @ApiResponse(code = 404, message = "Local não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Local> local = service.getLocalById(id);

        if (!local.isPresent()){
            return new ResponseEntity("Local não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(local.map(LocalDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar um novo local")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Local salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar um local")
    })
    public ResponseEntity post(@RequestBody LocalDto dto) {
        try {
            Local local = converter(dto);
            local = service.salvar(local);
            return new ResponseEntity(local, HttpStatus.CREATED);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar uma nova lesão")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Local editado com sucesso"),
            @ApiResponse(code = 404, message = "Local não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao salvar um local")
    })
    public ResponseEntity atualizar(@PathVariable("id") long id, @RequestBody LocalDto dto){
        if(!service.getLocalById(id).isPresent()){
            return new ResponseEntity("Local não encontrado", HttpStatus.NOT_FOUND);
        }
        try{
            Local local = converter(dto);
            local.setId(id);
            service.salvar(local);
            return ResponseEntity.ok(local);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar um local")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Local deletado com sucesso"),
            @ApiResponse(code = 404, message = "Local não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao deletar uma local")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id){
        Optional<Local> local = service.getLocalById(id);
        if(!local.isPresent()){
            return new ResponseEntity("Local não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(local.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Local converter(LocalDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Local local = modelMapper.map(dto, Local.class);
        return local;
    }
}
