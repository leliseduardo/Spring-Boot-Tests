package com.example.scaapi.api.controller;

import com.example.scaapi.api.dto.LoginDto;
import com.example.scaapi.api.dto.TokenDto;
import com.example.scaapi.api.dto.UsuarioDto;
import com.example.scaapi.config.security.JwtService;
import com.example.scaapi.config.security.UsuarioDetailsService;
import com.example.scaapi.enums.RoleName;
import com.example.scaapi.model.entity.Role;
import com.example.scaapi.model.entity.Usuario;
import com.example.scaapi.model.repository.UsuarioRepository;
import com.example.scaapi.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/usuarios")
@Api(tags = "API de Usuários")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioDetailsService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioController(UsuarioRepository usuarioRepository, UsuarioDetailsService userService, RoleService roleService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    @ApiOperation("Obter detalhes de todos os usuários")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuários encontrados"),
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido")
    })
    public ResponseEntity get() {
        List<Usuario> usuarios = userService.getUsuarios();

        List<UsuarioDto> dtos = usuarios.stream().map(user -> {
            UsuarioDto dto = new UsuarioDto();
            dto.setRoleNames(user.getRoleNames());
            dto.setId(user.getId());
            dto.setPassword(user.getPassword());
//            dto.setPassword("***");
            dto.setUsername(user.getUsername());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um único usuário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário encontrado"),
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido")
    })
    public ResponseEntity get(@PathVariable("id") UUID id) {
        Optional<Usuario> user = userService.findById(id);

        if (!user.isPresent()){
            return new ResponseEntity("User não encontrado", HttpStatus.NOT_FOUND);
        }

        UsuarioDto dto = new UsuarioDto();
        dto.setId(user.get().getId());
        dto.setPassword(user.get().getPassword());
//        dto.setPassword("***");
        dto.setUsername(user.get().getUsername());
        dto.setRoleNames(user.get().getRoleNames());

        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ApiOperation("Criar um novo usuário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário criado"),
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido")
    })
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UsuarioDto userDto){

        try {
            Usuario userModel = converter(userDto); // Converte DTO para Usuario e cria Usuario
            for (RoleName role : userDto.getRoleNames()) {
                Role roleModel = roleService.getByRoleName(role);
                roleModel.getUsuarios().add(userModel);
                userModel.getRoles().add(roleModel);
            }
            userModel.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userModel = userService.save(userModel);
            userDto.setId(userModel.getId());
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody @Valid LoginDto loginDto){

            Optional<Usuario> usuario = usuarioRepository.findByUsername(loginDto.getUsername());
            String token;

            if(!usuario.isPresent())
                return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
            else {
                if (senhasIguais(loginDto.getPassword(), usuario.get().getPassword())) {
                    token = jwtService.gerarToken(usuario.get());
//                    return ResponseEntity.ok(new TokenDto(usuario.get().getUsername(), token));
                    return ResponseEntity.ok(usuario.get().getRoleNames());
                }
                else
                    return new ResponseEntity("Senha incorreta", HttpStatus.FORBIDDEN);
            }

    }


    @PutMapping("/{id}")
    @ApiOperation("Atualizar os dados de um usuário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário atualizado"),
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido")
    })
    public ResponseEntity atualizar(@PathVariable("id") UUID id, @RequestBody UsuarioDto userDto){
        Optional<Usuario> user = userService.findById(id);
        if(!user.isPresent()){
            return new ResponseEntity("Jogador não encontrado", HttpStatus.NOT_FOUND);
        }
        try{
            Usuario userModel = converter(userDto);
            for (RoleName role : userDto.getRoleNames()) {
                Role roleModel = roleService.getByRoleName(role);
                roleModel.getUsuarios().add(userModel);
                userModel.getRoles().add(roleModel);
            }
            userModel.setId(id);
            userDto.setId(userModel.getId());
            userDto.setPassword(userModel.getPassword());
            userDto.setUsername(userModel.getUsername());
            userDto.setRoleNames(userModel.getRoleNames());
//            userModel.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userService.save(userModel);
            return ResponseEntity.ok(userDto);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Excluir um usuário")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Usuário deletado"),
            @ApiResponse(code = 401, message = "Login não autorizado"),
            @ApiResponse(code = 403, message = "Login proibido")
    })
    public ResponseEntity excluir(@PathVariable("id") UUID id){
        Optional<Usuario> user = userService.findById(id);
        if(!user.isPresent()){
            return new ResponseEntity("Jogador não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            UsuarioDto dto = new UsuarioDto();
            dto.setId(user.get().getId());
            dto.setPassword("Usuário excluído");
            dto.setUsername(user.get().getUsername());
            dto.setRoleNames(user.get().getRoleNames());
            userService.delete(user.get());
            return ResponseEntity.ok(dto);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public boolean senhasIguais(String senhaDigitada, String senhaArmazenada){
        return passwordEncoder.matches(senhaDigitada, senhaArmazenada);
    }

    public Usuario converter(UsuarioDto dto) {
        ModelMapper modelMapper = new ModelMapper();
        dto.setRoleNames(dto.getRoleNames().stream().map(role -> RoleName.valueOf(String.valueOf(role))).collect(Collectors.toList()));
        Usuario user = new Usuario();
        user = modelMapper.map(dto, Usuario.class);
        return user;
    }
}
