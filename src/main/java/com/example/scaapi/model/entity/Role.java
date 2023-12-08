package com.example.scaapi.model.entity;

import com.example.scaapi.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(unique = true)
    private RoleName roleName;
    @ManyToMany(mappedBy = "roles")
    private List<Usuario> usuarios = new ArrayList<Usuario>();

    @Override
    public String getAuthority() {
        return this.roleName.toString();
    }
}











