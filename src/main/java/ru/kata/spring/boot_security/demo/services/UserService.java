package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Collection;
import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> listUser ();
    void addUser (User user);
    void updateUser (User user);
    User getUserById(int id);
    void removeUser (int id);
    Collection<Role> getSetOfRoles(List<String> role);
    String getCurrentUsername();
    User findByUsername(String name);

    @Override
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }


}

