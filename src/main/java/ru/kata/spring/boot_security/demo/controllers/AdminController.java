package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String getAllUsers(Model model, Principal principal) {
        User admin = userService.findByUsername(principal.getName());
        model.addAttribute("admin", admin);
        model.addAttribute("allUsers", userService.listUser());
        model.addAttribute("roles", roleService.getRoleList());
        return "admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @Valid @ModelAttribute("user") User user,
            @PathVariable("id") int id,
            @RequestParam(name = "roles", required = false) String[] roles
    ) {
//        Set<Role> roleSet = new HashSet<>();
//        if (roles == null) {
//            user.setRoles((Set<Role>) userService.getUserById(id).getRoles());
//        } else {
//            for (String role : roles) {
//                roleList.add(roleService.getRoleById(Integer.parseInt(role)));
//            }
//            user.setRoles(roleList);
//        }
//        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/registration")
    public String addUser(
            @Valid @ModelAttribute("user") User user,
            @RequestParam("roles") Set<String> roles
    ) {
        user.setRoles(userService.getSetOfRoles(roles));
        userService.updateUser(user);
        return "redirect:/admin";
    }
}