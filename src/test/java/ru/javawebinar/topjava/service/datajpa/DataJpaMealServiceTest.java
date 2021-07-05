package ru.javawebinar.topjava.service.datajpa;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static ru.javawebinar.topjava.MealTestData.MATCHER;

@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void getWithUser() {
        Meal mealToCompare = MealTestData.getWithUser();
        Meal meal = service.getWithUser(mealToCompare.id(), mealToCompare.getUser().id());
        MATCHER.assertMatch(meal, mealToCompare);
        UserTestData.MATCHER.assertMatch(meal.getUser(), mealToCompare.getUser());
    }
}
