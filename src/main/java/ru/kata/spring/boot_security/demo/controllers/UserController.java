package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kata.spring.boot_security.demo.services.UserService;


@Controller
public class UserController {
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String getUserInfo(Model model) {
        model.addAttribute("user", userService
                .loadUserByUsername(userService.getCurrentUsername()));
        return "user";
    }
    @GetMapping("/user/{username}")
    public String getUser(@PathVariable String username, Model model) {
        model.addAttribute("user", userService.loadUserByUsername(username));
        return "user";
    }
}