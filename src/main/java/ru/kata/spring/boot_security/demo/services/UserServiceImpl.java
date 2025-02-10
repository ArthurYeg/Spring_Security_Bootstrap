package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> listUser () {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void addUser (User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser (User user) {
        User existingUser  = userRepository.findById(user.getId()).orElse(null);
        if (existingUser  != null) {
            if (!user.getPassword().equals(existingUser .getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                user.setPassword(existingUser .getPassword());
            }
            userRepository.save(user);
        }
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void removeUser (long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Collection<Role> getSetOfRoles(List<String> role) {
        return null;
    }

    @Override
    public String getCurrentUsername() {
        return null;
    }

    @Override
    public User findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User  not found with username: " + username);
        }
        return user;
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Кодируем пароль
        userRepository.save(user);
    }

    @Override
    public boolean usernameExists(String username) {
        return false;
    }

    @Override
    public User findById(Long id) {
        return null;
    }
}
