package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

public class MealInputValidator implements Validator {

    private final MealService mealService;

    @Autowired
    public MealInputValidator(MealService mealService) {
        this.mealService = mealService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Meal.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        int userId = SecurityUtil.authUserId();
        Meal meal = (Meal) target;

        if (meal.getDateTime() != null && !errors.hasFieldErrors("dateTime")) {
            if (mealService.getBetweenInclusive(meal.getDate(), meal.getDate(), userId).stream()
                    .anyMatch(meal1 -> meal1.getDateTime().equals(meal.getDateTime()) && !meal1.getId().equals(meal.getId()))) {
                errors.rejectValue("dateTime", "meal.dateTime.duplicate", "Meal with such dateTime already exists");
            }
        }
    }
}
