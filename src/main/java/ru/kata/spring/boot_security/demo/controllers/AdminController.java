package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.AbstractBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String usersListPage(Model model, Principal principal) {
        model.addAttribute("newUser", new User());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getRoleList());
        return "admin";
    }

    @PostMapping()
    public String createUser(@Valid @ModelAttribute("newUser") User user, BindingResult bindingResult, Model model) {
        if (userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email already exists");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getRoleList());
            return "admin";
        }
        userService.createUser(user);
        return "redirect:/admin";

    }

    @PutMapping("{id}")
    public String editUser (@PathVariable Long id, @Valid @ModelAttribute("user") User user,
                            BindingResult bindingResult, Model model) {
        User existingUser  = userService.findById(id);

        if (!existingUser .getEmail().equals(user.getEmail())) {
            if (userService.emailExists(user.getEmail())) {
                bindingResult.rejectValue("email", "error.user", "Email already exists");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getRoleList());
            model.addAttribute("users", userService.getAllUsers());
            return "admin";
        }

        userService.editUser (user);
        return "redirect:/admin";
    }


    @DeleteMapping("admin/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}