package ru.kata.spring.boot_security.demo.validator;

import ru.kata.spring.boot_security.demo.models.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User ) target;

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            errors.rejectValue("username", "error.username", "Username is required");
        }

    }
}
