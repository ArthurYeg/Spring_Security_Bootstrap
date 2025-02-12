package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Collection;
import java.util.List;

public interface RoleService {
    List<Role> getSetOfRoles(List<String> rolesId);
    Role getRoleById(int id);
    List<Role> getRoleList();
}