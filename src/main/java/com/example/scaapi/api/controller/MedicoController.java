package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.MedicoDto;
import com.example.scaapi.api.dto.MedicoDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Medico;
import com.example.scaapi.service.MedicoService;
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
@RequestMapping("/api/v1/medicos")
@RequiredArgsConstructor
@Api(tags="API de Medicos")
public class MedicoController {
    private final MedicoService service;

    @GetMapping()
    @ApiOperation("Obter todos os médicos")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido"),
            @ApiResponse(code = 200, message = "Médicos encontrados")
    })
    public ResponseEntity get() {
        List<Medico> medicos = service.getMedicos();
        return ResponseEntity.ok(medicos.stream().map(MedicoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um médico")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido"),
            @ApiResponse(code = 200, message = "Médico encontrado"),
            @ApiResponse(code = 404, message = "Médico não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Medico> medico = service.getMedicoById(id);

        if (!medico.isPresent()){
            return new ResponseEntity("Medico não encontrada", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(medico.map(MedicoDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar um novo médico")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido"),
            @ApiResponse(code = 201, message = "Médico salvo com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar um médico")
    })
    public ResponseEntity post(@RequestBody MedicoDto dto) {
        try {
            Medico medico = converter(dto);
            medico = service.salvar(medico);
            return new ResponseEntity(medico, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar um médico")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido"),
            @ApiResponse(code = 200, message = "Médico editado com sucesso"),
            @ApiResponse(code = 404, message = "Médico não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao salvar um médico")
    })
    public ResponseEntity atualizar(@PathVariable("id") long id, @RequestBody MedicoDto dto){
        if(!service.getMedicoById(id).isPresent()){
            return new ResponseEntity("Medico não encontrado", HttpStatus.NOT_FOUND);
        }
        try{
            Medico medico = converter(dto);
            medico.setId(id);
            service.salvar(medico);
            return ResponseEntity.ok(medico);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar um médico")
    @ApiResponses({
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido"),
            @ApiResponse(code = 204, message = "Médico deletado com sucesso"),
            @ApiResponse(code = 404, message = "Médico não encontrado"),
            @ApiResponse(code = 400, message = "Erro ao deletar um médico")
    })
    public ResponseEntity excluir(@PathVariable("id") long id) {
        Optional<Medico> medico = service.getMedicoById(id);
        if (!medico.isPresent()) {
            return new ResponseEntity<>("Medico não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(medico.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    public Medico converter(MedicoDto dto){
        ModelMapper modelMapper = new ModelMapper();
        Medico medico = modelMapper.map(dto, Medico.class);
        return medico;
    }

}
