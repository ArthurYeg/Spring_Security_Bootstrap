package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer {

    private final UserRepository userRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void dataInitializer() {
        Set<Role> roleAdmin = new HashSet<>();
        Set<Role> roleUser = new HashSet<>();
        roleAdmin.add(new Role("ROLE_ADMIN"));
        roleUser.add(new Role("ROLE_USER"));
        User admin = new User("admin", (byte) 20, "$2a$12$boS3Oud9fYxdXMSA4SJcPu9nJUf0JRwP032PxmlR85bFrvM845rr2", "admin@mail.ru");
        User user= new User("user", (byte) 20, "$2a$12$boS3Oud9fYxdXMSA4SJcPu9nJUf0JRwP032PxmlR85bFrvM845rr2", "user@mail.ru");

        admin.setRoles(roleAdmin);
        user.setRoles(roleUser);

        userRepository.save(user);
        userRepository.save(admin);
    }
}