package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    public final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(Model model, Principal principal) {
        User admin = userService.findByUsername(principal.getName());
        model.addAttribute("admin", admin);
        model.addAttribute("allUsers", userService.listUser());
        model.addAttribute("roles", roleService.getRoleList());
        return "admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") long id,
                         @RequestParam(name = "roles", required = false) List<Long> roles) {
        List<Role> rolesList = new ArrayList<>();
        if (roles != null) {
            for (Long roleId : roles) {
                Role role = roleService.getRoleById(roleId);
                if (role != null) {
                    rolesList.add(role);
                }
            }
        } else {
            // Если роли не указаны, оставляем старые роли
            rolesList = new ArrayList<>(userService.getUserById(id).getRoles());
        }
        user.setRoles(rolesList);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam("roles") List<Long> roleIds,
                          Model model) {
        // Проверяем, существует ли пользователь с таким именем
        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("errorMessage", "A user with this name already exists.");
            model.addAttribute("roles", roleService.getRoleList());
            return "registration";
        }

        try {
            userService.addUser(user);
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "There was an error adding the user.");
            return "registration";
        }

        return "redirect:/admin";
    }
}