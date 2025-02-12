package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.validator.UserValidator;


@Controller
public class UserController {
    private final UserService userService;
    private final UserValidator userValidator;


    @Autowired
    public UserController(UserService userService,UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
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

    @PostMapping("/user/new")
    public String createNewUser(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "user/new";
        }

        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/user/edit/{username}")
    public String editUserForm(@PathVariable String username, Model model) {
        User user = (User) userService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "user/edit";
    }

    @PostMapping("/user/edit")
    public String editUser(@ModelAttribute("user") User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "user/edit";
        }

        userService.save(user);
        return "redirect:/users";
    }
}