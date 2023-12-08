package com.example.scaapi.model.repository;

import com.example.scaapi.enums.RoleName;
import com.example.scaapi.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role getByRoleName(RoleName roleName);
}
