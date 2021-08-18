package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

public class ProfileUserInputValidator implements Validator {

    private final UserService userService;

    @Autowired
    public ProfileUserInputValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String email = ((UserTo) target).getEmail();

        if (email != null && !errors.hasFieldErrors("email")) {
            User foundUser = userService.getByEmailNullable(email);
            Integer userId = null;
            if(SecurityUtil.safeGet() != null) {
                userId = SecurityUtil.authUserId();
            }
            if (foundUser != null && !foundUser.getId().equals(userId)) {
                errors.rejectValue("email", "userTo.email.duplicate", "User with this email already exists");
            }
        }
    }
}
