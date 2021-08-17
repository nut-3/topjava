package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

public class UserToValidator implements Validator {

    private final Validator validator;
    private final UserService userService;

    @Autowired
    public UserToValidator(Validator validator, UserService userService) {
        this.validator = validator;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.invokeValidator(validator, target, errors);

        UserTo userTo = (UserTo) target;
        String email = userTo.getEmail();
        if (email != null) {
            try {
                userService.getByEmail(email);
                errors.rejectValue("email", "userTo.email.duplicate");
            } catch (NotFoundException ignored) {
            }
        }
    }
}
