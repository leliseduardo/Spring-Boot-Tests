package com.example.scaapi.api.dto;

import com.example.scaapi.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {

    private UUID id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    private List<RoleName> roleNames;
}
