package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;


import javax.validation.ValidationException;
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
    @GetMapping("/update/{id}")
    public String getUpdateForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getRoleList());
        return "updateForm"; // Название шаблона для формы обновления
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<String> update(@ModelAttribute("user") User user,
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

            rolesList = new ArrayList<>(userService.getUserById(id).getRoles());
        }
        user.setRoles(rolesList);
        try {
            userService.updateUser(user);
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
             return (ResponseEntity<String>) ResponseEntity.status(HttpStatus.OK);
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam("roles") List<Long> roleIds,
                          Model model) {

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