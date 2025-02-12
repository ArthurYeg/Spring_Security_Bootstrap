package ru.kata.spring.boot_security.demo.configs;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserServiceImpl userServiceImpl, RoleRepository roleRepository) {
        return args -> {
            Role userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);

            Role adminRole = new Role("ROLE_ADMIN");
            roleRepository.save(adminRole);

            User user = new User();
            user.setUsername("user");
            user.setPassword("123");
            user.setAge(23);
            user.setRoles(List.of(userRole));
            userServiceImpl.addUser (user);

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("123");
            admin.setAge(23);
            admin.setRoles(List.of(adminRole, userRole));
            userServiceImpl.addUser (admin);
        };
    }
}
