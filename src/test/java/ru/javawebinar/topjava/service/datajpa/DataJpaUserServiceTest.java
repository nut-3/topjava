package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.Collections;

import static ru.javawebinar.topjava.UserTestData.*;


@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        User userToCompare = UserTestData.getWithMeals();
        MATCHER.assertMatch(user, userToCompare);
        MealTestData.MATCHER.assertMatch(user.getMeals(), userToCompare.getMeals());
    }

    @Test
    public void getWithoutMeals() {
        int userId = service.create(getNew()).id();
        User userToCompare = getNew();
        userToCompare.setId(userId);
        User user = service.getWithMeals(userId);
        MATCHER.assertMatch(user, userToCompare);
        MealTestData.MATCHER.assertMatch(user.getMeals(), Collections.emptyList());
    }
}
