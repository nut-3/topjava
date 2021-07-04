package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.MATCHER_USER;
import static ru.javawebinar.topjava.MealTestData.getMealWithUser;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void getWithUser() {
        Meal meal = getMealWithUser();
        MATCHER_USER.assertMatch(service.getWithUser(meal.id(), meal.getUser().id()), meal);
    }
}
