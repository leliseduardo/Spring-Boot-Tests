package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.PosicaoDto;
import com.example.scaapi.api.dto.PosicaoDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.Posicao;
import com.example.scaapi.model.entity.Posicao;
import com.example.scaapi.service.PosicaoService;
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
@RequestMapping("/api/v1/posicoes")
@RequiredArgsConstructor
@Api(tags="API de Posicoes")
public class PosicaoController {
    private final PosicaoService service;

    @GetMapping()
    @ApiOperation("Obter todos as posições")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Posições encontradas")
    })
    public ResponseEntity get() {
        List<Posicao> posicoes = service.getPosicoes();
        return ResponseEntity.ok(posicoes.stream().map(PosicaoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de uma posição")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Posição encontrado"),
            @ApiResponse(code = 404, message = "Posição não encontrado")
    })
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Posicao> posicao = service.getPosicaoById(id);

        if (!posicao.isPresent()){
            return new ResponseEntity("Posição não encontrada", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(posicao.map(PosicaoDto::create));
    }

    @GetMapping("/{id}/jogadores")
    @ApiOperation("Obter lista de jogadores de uma posição")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Posição encontrado"),
            @ApiResponse(code = 404, message = "Posição não encontrado")
    })
    public ResponseEntity getJogador(@PathVariable("id") Long id) {
        Optional<Posicao> posicao = service.getPosicaoById(id);

        if (!posicao.isPresent()){
            return new ResponseEntity("Posição não encontrada", HttpStatus.NOT_FOUND);
        }

        List<String> jogadores = posicao.get().getJogadores().stream().map(
                jogador -> {return jogador.getNome();
                }).collect(Collectors.toList());

        return ResponseEntity.ok(jogadores);
    }

    @PostMapping()
    @ApiOperation("Salvar uma nova posição")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Posição salva com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar um posição")
    })
    public ResponseEntity post(@RequestBody PosicaoDto dto) {
        try {
            Posicao posicao = converter(dto);
            posicao = service.salvar(posicao);
            dto = PosicaoDto.create(posicao);
            return new ResponseEntity(dto, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("Atualizar uma posicao")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Posicao salva com sucesso"),
            @ApiResponse(code = 404, message = "Posicao não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao salvar a posicao")
    })
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody PosicaoDto dto){
        if(!service.getPosicaoById(id).isPresent()){
            return new ResponseEntity("Posicao não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Posicao posicao = converter(dto);
            posicao.setId(id);
            service.salvar(posicao);
            dto = PosicaoDto.create(posicao);
            return ResponseEntity.ok(dto);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletar uma posição")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Posição deletada com sucesso"),
            @ApiResponse(code = 404, message = "Posição não encontrada"),
            @ApiResponse(code = 400, message = "Erro ao deletar uma posição")
    })
    public ResponseEntity excluir(@PathVariable("id") long id) {
        Optional<Posicao> posicao = service.getPosicaoById(id);
        if (!posicao.isPresent()) {
            return new ResponseEntity<>("Posição não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(posicao.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    public Posicao converter(PosicaoDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        Posicao posicao = new ModelMapper().map(dto, Posicao.class);
        return posicao;
    }
}
