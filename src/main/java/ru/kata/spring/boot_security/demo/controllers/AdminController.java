package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
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
        model.addAttribute("allUsers", userService.listUser ());
        model.addAttribute("roles", roleService.getRoleList());
        return "admin";
    }


    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.removeUser(id);
        return "redirect:/admin";
    }
//
//    @GetMapping("update/{id}")
//    public String updateUserForm(@PathVariable("id") int id, Model model) {
//        model.addAttribute("user", userService.getUserById(id));
//        model.addAttribute("allRoles", roleService.getRoleList());
//        return "update";
//    }

    @PostMapping("/update/{id}")
    public String update
            (@ModelAttribute("user") User user, @PathVariable("id") int id, @RequestParam(name = "roles", required = false) String[] roles) {
        List<Role> roles1 = new ArrayList<>();
        if (roles == null) {
            user.setRoles((List<Role>) userService.getUserById(id).getRoles());
        } else {
            for (String role : roles) {
                roles1.add(roleService.getRoleById(id));
                user.setRoles(roles1);
            }
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user") User user, @RequestParam("roles") List<String> role) {
        user.setRoles(userService.getSetOfRoles(role));
        userService.updateUser(user);
        return "redirect:/admin";
    }
}