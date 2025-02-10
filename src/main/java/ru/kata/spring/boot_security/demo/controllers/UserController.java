package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder; // Внедрение PasswordEncoder

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder; // Инициализация
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser (@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        try {
            userService.save(user);
            return ResponseEntity.ok("User  registered successfully");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username already exists");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/user")
    public String getUserInfo(Model model) {
        model.addAttribute("user", userService.loadUserByUsername(userService.getCurrentUsername()));
        return "user";
    }

    @GetMapping("/user/{username}")
    public String getUser (@PathVariable String username, Model model) {
        model.addAttribute("user", userService.loadUserByUsername(username));
        return "user";
    }
}
