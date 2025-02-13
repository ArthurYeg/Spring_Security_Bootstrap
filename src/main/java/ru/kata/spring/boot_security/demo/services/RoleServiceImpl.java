package ru.kata.spring.boot_security.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

   RoleRepository roleRepository;


    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getSetOfRoles(List<String> rolesId) {
        return List.of();
    }

    @Override
    public Role add(Role role){
        return roleRepository.save(role);
    }

    @Override
    public Role getRoleById(Long id) {
        return null;
    }

    @Override
    public List<Role> getRoleList() {
        return roleRepository.findAll();
    }
}