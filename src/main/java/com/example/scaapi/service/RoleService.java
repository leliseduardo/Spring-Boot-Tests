package com.example.scaapi.service;

import com.example.scaapi.enums.RoleName;
import com.example.scaapi.model.entity.Role;
import com.example.scaapi.model.repository.RoleRepository;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {

    final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getByRoleName(RoleName roleName){ return roleRepository.getByRoleName(roleName); }

    @Transactional
    public Role save(Role role) { return roleRepository.save(role); }

    public Optional<Role> findById(UUID id) {
        return roleRepository.findById(id);
    }

    @Transactional
    public void delete(Role role) {
        roleRepository.delete(role);
    }
}


















