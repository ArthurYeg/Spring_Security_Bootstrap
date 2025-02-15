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
        Set<Role> roleKata = new HashSet<>();
        Set<Role> roleArthur = new HashSet<>();
        roleKata.add(new Role("ROLE_ADMIN"));
        roleArthur.add(new Role("ROLE_USER"));
        User kata = new User("kata", "kata_name", (byte) 20, "$2a$12$boS3Oud9fYxdXMSA4SJcPu9nJUf0JRwP032PxmlR85bFrvM845rr2", "kata@mail.ru");
        User arthur = new User("arthur", "arthur_name", (byte) 20, "$2a$12$boS3Oud9fYxdXMSA4SJcPu9nJUf0JRwP032PxmlR85bFrvM845rr2", "arthur@mail.ru");

        kata.setRoles(roleKata);
        arthur.setRoles(roleArthur);

        userRepository.save(arthur);
        userRepository.save(kata);
    }
}