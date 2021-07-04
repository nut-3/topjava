package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;


@ActiveProfiles(Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        MATCHER_MEALS.assertMatch(user, UserTestData.userWithMeals);
    }

    @Test
    public void getWithoutMeals() {
        User userWithoutMeals = service.create(getNew());
        User userToCompare = getNew();
        userToCompare.setId(userWithoutMeals.id());
        MATCHER_MEALS.assertMatch(service.getWithMeals(userWithoutMeals.id()), userToCompare);
    }
}
