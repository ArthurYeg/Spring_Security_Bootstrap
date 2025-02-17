package ru.kata.spring.boot_security.demo.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public User findByUsername(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User  not found with ID: " + id));
    }

    @Override
    public boolean createUser(@ModelAttribute User user) {
        if (userRepository.findByUsername(user.getEmail()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User getEditUserPage(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }

    @Override
    @Transactional
    public boolean editUser (@ModelAttribute("user") User user) {
        return userRepository.findById(user.getId())
                .map(existingUser  -> {
                    existingUser .setUsername(user.getUsername());
                    existingUser .setAge(user.getAge());

                    if (!existingUser .getEmail().equals(user.getEmail())) {
                        existingUser .setEmail(user.getEmail());
                    }

                    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        existingUser .setPassword(passwordEncoder.encode(user.getPassword()));
                    }

                    existingUser .setRoles(user.getRoles());
                    userRepository.save(existingUser );
                    return true;
                })
                .orElse(false);
    }


    @Override
    public boolean deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRoles(null);
                    userRepository.delete(user);
                    return true;
                })
                .orElse(false);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByUsername(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user.getRoles()));
    }

    public Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
    }
}