package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Objects;

public class InputValidator implements Validator {

    private final Validator validator;
    private final UserService userService;
    private final MealService mealService;

    @Autowired
    public InputValidator(Validator validator, UserService userService, MealService mealService) {
        this.validator = validator;
        this.userService = userService;
        this.mealService = mealService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.isAssignableFrom(clazz) || User.class.isAssignableFrom(clazz) || Meal.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.invokeValidator(validator, target, errors);

        if (target instanceof User || target instanceof UserTo) {
            String email = null;

            if (target instanceof UserTo) {
                email = ((UserTo) target).getEmail();
            }
            if (target instanceof User) {
                email = ((User) target).getEmail();
            }

            if (email != null) {
                try {
                    userService.getByEmail(email);
                    errors.rejectValue("email", "userTo.email.duplicate", "User with this email already exists");
                } catch (NotFoundException ignored) {
                }
            }
        }

        if (target instanceof Meal) {
            int userId = SecurityUtil.authUserId();
            Meal meal = (Meal) target;

            if (mealService.getAll(userId).stream()
                    .anyMatch(meal1 -> Objects.equals(meal.getDateTime(), meal1.getDateTime()))) {
                errors.rejectValue("dateTime", "meal.dateTime.duplicate", "Meal with such dateTime already exists");
            }
        }
    }
}
