package com.example.scaapi.api.controller;


import com.example.scaapi.api.dto.RelacionadoDto;
import com.example.scaapi.exception.RegraNegocioException;
import com.example.scaapi.model.entity.*;
import com.example.scaapi.service.JogadorService;
import com.example.scaapi.service.RelacionadosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/relacionados")
@RequiredArgsConstructor
@Api(tags="API de Relacionados na Partida")
public class RelacionadoController {
    private final RelacionadosService service;
    private final JogadorService jogadorService;

    @GetMapping
    @ApiOperation("Obter todas os jogadores relacionados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogadores relacionados encontrados")
    })
    public ResponseEntity get(){
        List<Relacionado> relacionados = service.getRelacionados();
        return ResponseEntity.ok(relacionados.stream().map(RelacionadoDto::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de Jogadores relacionados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogadores relacionados encontrados"),
            @ApiResponse(code = 404, message = "Jogadores relacionados não encontrados")
    })
    public ResponseEntity getById(@PathVariable long id){
        Optional<Relacionado> relacionados = service.getRelacionadosById(id);

        if(!relacionados.isPresent()){
            return new ResponseEntity("Jogadores relacionados não encontrados", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(relacionados.map(RelacionadoDto::create));
    }

    @PostMapping()
    @ApiOperation("Salvar novos Jogadores relacionados")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Jogadores relacionados salvos com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar Jogadores relacionaods")
    })
    public ResponseEntity post(@RequestBody RelacionadoDto dto){
        try{
            Relacionado relacionado = converter(dto);
            relacionado = service.salvar(relacionado);
            return new ResponseEntity(relacionado, HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/partida")
    @ApiOperation("Salvar novos Jogadores relacionados")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Jogadores relacionados salvos com sucesso"),
            @ApiResponse(code = 400, message = "Erro ao salvar Jogadores relacionaods")
    })
    public ResponseEntity postRelacionadosNaPartida(@RequestBody Map dto){
        try{
            ArrayList<String> titularesId = (ArrayList<String>) dto.get("titulares");
            ArrayList<String> reservasId = (ArrayList<String>) dto.get("reservas");
            String partida = (String) dto.get("partidaId");

            //@todo: Verificar se a partida existe no banco de dados
            if (partida.equals("")){
                throw new RegraNegocioException("Partida invalida");
            }

            //@todo: Pegar os relacionados da partida e remover todos
            if (titularesId.size() != 11){
                throw new RegraNegocioException("Uma Partida precisa ter 11 titulares");
            }

            Long partidaId = Long.parseLong(partida);

            for (String s : titularesId) {
                Long titularId = Long.parseLong(s);
                service.salvar(converter(new RelacionadoDto(true, partidaId, titularId)));
            }

            for (String s : reservasId) {
                Long reservaId = Long.parseLong(s);
                service.salvar(converter(new RelacionadoDto(false, partidaId, reservaId)));
            }

            return new ResponseEntity("Jogadores relacionados salvos com sucesso", HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/partida/{id}")
    @ApiOperation("Obter detalhes de Jogadores relacionados")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogadores relacionados encontrados"),
            @ApiResponse(code = 404, message = "Jogadores relacionados não encontrados")
    })
    public ResponseEntity getByPartidaId(@PathVariable long id){
        List<Relacionado> relacionados = service.getRelacionadosByPartidaId(id);

        if(relacionados.isEmpty()){
            return new ResponseEntity("Relacionados para partida não encontrados", HttpStatus.NOT_FOUND);
        }

        Map<String, Object> response = new HashMap<>();

        Partida partida = relacionados.get(0).getPartida();
        Campeonato campeonato = partida.getCampeonato();
        Temporada temporada = campeonato.getTemporada();

        response.put("partida", partida.getId());
        response.put("campeonato", campeonato.getId());
        response.put("temporada", temporada.getId());

        Map<Long, Object> titulares = new HashMap<>();
        Map<Long, Object> reservas = new HashMap<>();
        List<Long> jogadoresRelacionadosId = new ArrayList<>();

        relacionados.forEach(relacionado -> {
            Map<String, Object> formattedPlayer = new HashMap<>();
            Jogador jogador = relacionado.getJogador();
            Long jogadorId = jogador.getId();
            String name = jogador.getNome();
            Boolean isTitular = relacionado.getTitular();


            formattedPlayer.put("label", name);
            formattedPlayer.put("checked", false);

            if (isTitular) {
                titulares.put(jogadorId, formattedPlayer);
            }else {
                reservas.put(jogadorId, formattedPlayer);
            }

            jogadoresRelacionadosId.add(jogadorId);
        });

        response.put("titulares", titulares);
        response.put("reservas", reservas);

        List<Jogador> allPlayers = jogadorService.getJogadores();
        Map<Long, Object> players = allPlayers.stream()
                .filter(player -> !jogadoresRelacionadosId.contains(player.getId()))
                .collect(Collectors.toMap(Jogador::getId, player -> {
                    Map<String, Object> formattedPlayer = new HashMap<>();
                    formattedPlayer.put("label", player.getNome());
                    formattedPlayer.put("checked", false);
                    return formattedPlayer;
                }));
        response.put("jogadores", players);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @ApiOperation("Editar Jogadores relacionaods")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogadores relacionados editados com sucesso"),
            @ApiResponse(code = 404, message = "Jogadores relacionados não encontrados"),
            @ApiResponse(code = 400, message = "Erro ao salvar Jogadores relacionados")
    })
    public ResponseEntity atualizar(@PathVariable("id") long id, @RequestBody RelacionadoDto dto){
        if(!service.getRelacionadosById(id).isPresent()){
            return new ResponseEntity("Jogadores relacionados não encontrados", HttpStatus.NOT_FOUND);
        }
        try{
            Relacionado relacionado = converter(dto);
            relacionado.setId(id);
            service.salvar(relacionado);
            return ResponseEntity.ok(relacionado);
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/partida/{id}")
    @ApiOperation("Editar Jogadores relacionaods")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Jogadores relacionados editados com sucesso"),
            @ApiResponse(code = 404, message = "Jogadores relacionados não encontrados"),
            @ApiResponse(code = 400, message = "Erro ao salvar Jogadores relacionados")
    })
    public ResponseEntity atualizarRelacionadoNaPartida(@PathVariable("id") long id, @RequestBody Map dto){
        List<Relacionado> relacionados = service.getRelacionadosByPartidaId(id);

        if(relacionados.isEmpty()){
            return new ResponseEntity("Relacionados para partida não encontrados", HttpStatus.NOT_FOUND);
        }

        List<Long> atualTitulares = new ArrayList<>();
        List<Long> atualReservas = new ArrayList<>();
        Map<Long, Long> relacionadoIdPorJogadorId = new HashMap<>();

        relacionados.forEach(relacionado -> {
            Jogador jogador = relacionado.getJogador();
            Long jogadorId = jogador.getId();
            Boolean isTitular = relacionado.getTitular();

            if (isTitular) {
                atualTitulares.add(jogadorId);
            }else {
                atualReservas.add(jogadorId);
            }

            relacionadoIdPorJogadorId.put(jogadorId, relacionado.getId());
        });


        ArrayList<Long> requestTitulares = new ArrayList<>();
        ArrayList<Long> requestReservas = new ArrayList<>();

        for (String s : (ArrayList<String>) dto.get("titulares")) {
            Long titularId = Long.parseLong(s);
            requestTitulares.add(titularId);
        }

        for (String s : (ArrayList<String>) dto.get("reservas")) {
            Long reservaId = Long.parseLong(s);
            requestReservas.add(reservaId);
        }

        List<Long> titularesToRemove = arrayDiff(atualTitulares, requestTitulares);
        List<Long> titularesToAdd = arrayDiff(requestTitulares, atualTitulares);

        List<Long> reservasToRemove = arrayDiff(atualReservas, requestReservas);
        List<Long> reservasToAdd = arrayDiff(requestReservas, atualReservas);

        for (Long jogadorId : titularesToAdd){
            service.salvar(converter(new RelacionadoDto(true, id, jogadorId)));
        }

        for (Long jogadorId : reservasToAdd){
            service.salvar(converter(new RelacionadoDto(false, id, jogadorId)));
        }

        for (Long jogadorId : titularesToRemove){
            Optional<Relacionado> relacionado = service.getRelacionadosById(relacionadoIdPorJogadorId.get(jogadorId));
            relacionado.ifPresent(service::excluir);
        }

        for (Long jogadorId : reservasToRemove){
            Optional<Relacionado> relacionado = service.getRelacionadosById(relacionadoIdPorJogadorId.get(jogadorId));
            relacionado.ifPresent(service::excluir);
        }

        try{
            return ResponseEntity.ok("Jogadores relacionados editados com sucesso");
        }catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("id")
    @ApiOperation("Deletar Jogadores relacionados")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Jogadores relacionados deletados com sucesso"),
            @ApiResponse(code = 404, message = "Jogadores relacionados não encontrados"),
            @ApiResponse(code = 400, message = "Erro ao deletar Jogadores relacionados")
    })
    public ResponseEntity excluir(@PathVariable("id") long id) {
        Optional<Relacionado> relacionados = service.getRelacionadosById(id);
        if (!relacionados.isPresent()) {
            return new ResponseEntity<>("Jogadores relacionados não encontrados", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(relacionados.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    private Relacionado converter(RelacionadoDto dto){
        ModelMapper modelMapper = new ModelMapper();
        Relacionado relacionado = modelMapper.map(dto, Relacionado.class);
        return relacionado;
    }

    private <T> List<T> arrayDiff(List<T> list1, List<T> list2) {
        List<T> result = new ArrayList<>();

        for (T item : list1) {
            if (!list2.contains(item)) {
                result.add(item);
            }
        }

        return result;
    }
}
