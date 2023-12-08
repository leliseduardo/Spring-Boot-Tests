package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.CampeonatoDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Campeonato;
import com.example.scaapi.service.CampeonatoService;
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
@RequestMapping("/api/v1/campeonatos")
@RequiredArgsConstructor
@Api(tags="API de Campeonatos")
public class CampeonatoController {
    private final CampeonatoService service;

    @GetMapping()
    @ApiOperation("Obter todos os campeonatos")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Campeonatos encontrados")
    })
    public ResponseEntity get() {
        List<Campeonato> campeonatos = service.getCampeonato();
        return ResponseEntity.ok(campeonatos.stream().map(CampeonatoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um campeonato")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido"),
            @ApiResponse(code = 200, message = "Campeonato encontrado"),
            @ApiResponse(code = 404, message = "Campeonato não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Campeonato> campeonato = service.getCampeonatoById(id);

        if (!campeonato.isPresent()){
            return new ResponseEntity("Campeonato não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(campeonato.map(CampeonatoDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar um novo campeonato")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido"),
            @ApiResponse(code = 201, message = "Campeonato salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar um campeonato")
    })
    public ResponseEntity post (@RequestBody CampeonatoDto dto) {
        try {
            Campeonato campeonato = converter(dto);
            campeonato = service.salvar(campeonato);
            return new ResponseEntity(campeonato, HttpStatus.CREATED);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar um campeonato")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido"),
            @ApiResponse(code = 200, message = "Campeonato editado com sucesso"),
            @ApiResponse(code = 404, message = "Campeonato não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao salvar um campeonato")
    })
    public ResponseEntity atualizar(@PathVariable("id") long id, @RequestBody CampeonatoDto dto){
        if(!service.getCampeonatoById(id).isPresent()){
            return new ResponseEntity("Campeonato não encontrado", HttpStatus.NOT_FOUND);
        }
        try{
            Campeonato campeonato = converter(dto);
            campeonato.setId(id);
            service.salvar(campeonato);
            return ResponseEntity.ok(campeonato);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar um campeonato")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido"),
            @ApiResponse(code = 204, message = "Campeonato deletado com sucesso"),
            @ApiResponse(code = 404, message = "Campeonato não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao deletar um campeonato")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id){
        Optional<Campeonato> campeonato = service.getCampeonatoById(id);
        if(!campeonato.isPresent()){
            return new ResponseEntity("Campeonato não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(campeonato.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Campeonato converter(CampeonatoDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Campeonato campeonato = modelMapper.map(dto, Campeonato.class);
        return campeonato;
    }
}
