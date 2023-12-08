package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.LesaoJogadorDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.LesaoJogador;
import com.example.scaapi.service.LesaoJogadorService;
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
@RequestMapping("/api/v1/lesoes-jogador")
@RequiredArgsConstructor
@Api(tags="API de Lesoes do Jogador")
public class LesaoJogadorController {
    private final LesaoJogadorService service;

    @GetMapping()
    @ApiOperation("Obter todas as lesões do jogadores")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lesões do jogadores não encontradas")
    })
    public ResponseEntity get() {
        List<LesaoJogador> lesoesDoJogador = service.getLesoesDoJogador();
        return ResponseEntity.ok(lesoesDoJogador.stream().map(LesaoJogadorDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma lesão do jogador")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lesão do jogador encontrada"),
            @ApiResponse(code = 404, message = "Lesão do jogador não encontrada")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<LesaoJogador> lesaoJogador = service.getLesaoJogadorById(id);

        if (!lesaoJogador.isPresent()){
            return new ResponseEntity("Lesão do Jogador não encontrada", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(lesaoJogador.map(LesaoJogadorDto::create));
    }


    @PostMapping()
    @ApiOperation("Salvar uma nova lesão do jogador")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Lesão do jogador salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar uma lesão do jogador")
    })
    public ResponseEntity post(@RequestBody LesaoJogadorDto dto) {
        try {
            LesaoJogador lesaoJogador = converter(dto);
            lesaoJogador = service.salvar(lesaoJogador);
            return new ResponseEntity(lesaoJogador, HttpStatus.CREATED);
        }catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar uma lesão de um jogador")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Lesão do jogador editada com sucesso"),
            @ApiResponse(code = 404, message = "Lesão do jogador não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao salvar uma lesão do jogador")
    })
    public ResponseEntity atualizar(@PathVariable("id") long id, @RequestBody LesaoJogadorDto dto){
        if(!service.getLesaoJogadorById(id).isPresent()){
            return new ResponseEntity("LesaoJogador não encontrado", HttpStatus.NOT_FOUND);
        }
        try{
            LesaoJogador lesaoJogador = converter(dto);
            lesaoJogador.setId(id);
            service.salvar(lesaoJogador);
            return ResponseEntity.ok(lesaoJogador);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar um lesão de um jogador")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Lesão do jogador deletada com sucesso"),
            @ApiResponse(code = 404, message = "Lesão do jogador não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao deletar uma lesão do jogador")
    })
    public ResponseEntity excluir(@PathVariable("id") Long id){
        Optional<LesaoJogador> lesaoJogador = service.getLesaoJogadorById(id);
        if(!lesaoJogador.isPresent()){
            return new ResponseEntity("Lesao do jogador não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(lesaoJogador.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public LesaoJogador converter(LesaoJogadorDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        LesaoJogador lesaoJogador = modelMapper.map(dto, LesaoJogador.class);
        return lesaoJogador;
    }
}
