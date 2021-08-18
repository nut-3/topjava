package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;

public class AdminUserInputValidator implements Validator {

    private final UserService userService;

    @Autowired
    public AdminUserInputValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz) || User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String email = null;
        Integer userId = null;

        if (target instanceof UserTo) {
            email = ((UserTo) target).getEmail();
            userId = ((UserTo) target).getId();
        }
        if (target instanceof User) {
            email = ((User) target).getEmail();
            userId = ((User) target).getId();
        }

        if (email != null && !errors.hasFieldErrors("email")) {
            User foundUser = userService.getByEmailNullable(email);
            if (foundUser != null && !foundUser.getId().equals(userId)) {
                errors.rejectValue("email", "userTo.email.duplicate", "User with this email already exists");
            }
        }
    }
}
