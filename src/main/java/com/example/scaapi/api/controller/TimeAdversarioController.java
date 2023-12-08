package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.TimeAdversarioDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Temporada;
import com.example.scaapi.model.entity.TimeAdversario;
import com.example.scaapi.service.TimeAdversarioService;
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
@RequestMapping("/api/v1/times-adversarios")
@RequiredArgsConstructor
@Api(tags="API de Times Adversarios")
public class TimeAdversarioController {
    private final TimeAdversarioService service;

    @GetMapping
    @ApiOperation("Obter todos os Times Adversários")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Times Adversários encontrados")
    })
    public ResponseEntity get(){
        List<TimeAdversario> timesAdversarios = service.getTimeAdversario();
        return ResponseEntity.ok(timesAdversarios.stream().map(TimeAdversarioDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes do Time Adversário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Time Adversário encontrado"),
            @ApiResponse(code = 404, message = "Time Adversário não encontrado")
    })
    public ResponseEntity getById(@PathVariable long id){
        Optional<TimeAdversario> timeAdversario = service.getTimeAdversarioById(id);

        if(!timeAdversario.isPresent()){
            return new ResponseEntity("Time Adversario não encontrada", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(timeAdversario.map(TimeAdversarioDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar novo Time Adversário")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Time Adversário salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar Time Adversário")
    })
    public ResponseEntity post(@RequestBody TimeAdversarioDto dto){
        try{
            TimeAdversario timeAdversario = converter(dto);
            timeAdversario = service.salvar(timeAdversario);
            return new ResponseEntity(timeAdversario, HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar Time Adversário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Time Adversário editado com sucesso"),
            @ApiResponse(code = 404, message = "Time Adversário não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao salvar Time Adversário")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody TimeAdversarioDto dto) {
        if(!service.getTimeAdversarioById(id).isPresent()){
            return new ResponseEntity("Time Adversario não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            TimeAdversario timeAdversario = converter(dto);
            timeAdversario.setId(id);
            service.salvar(timeAdversario);
            return ResponseEntity.ok(timeAdversario);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar Time Adversário")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Time Adversário deletado com sucesso"),
            @ApiResponse(code = 404, message = "Time Adversário não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao deletar Time Adversário")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<TimeAdversario> timeAdversario = service.getTimeAdversarioById(id);
        if(!timeAdversario.isPresent()) {
            return new ResponseEntity("Time Adversario não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(timeAdversario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private TimeAdversario converter(TimeAdversarioDto dto){
        ModelMapper modelMapper = new ModelMapper();
        TimeAdversario timeAdversario = modelMapper.map(dto, TimeAdversario.class);
        return timeAdversario;
    }
}
