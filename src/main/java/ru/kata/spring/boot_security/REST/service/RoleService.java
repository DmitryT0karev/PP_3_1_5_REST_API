package ru.kata.spring.boot_security.REST.service;

import ru.kata.spring.boot_security.REST.model.Role;

import java.util.Set;


public interface RoleService {

    Set<Role> getAllRoles();

    Role findRoleById(Long id);
}
