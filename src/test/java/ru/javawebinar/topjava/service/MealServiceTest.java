package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;


@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        assertMatch(service.get(ADMIN_LUNCH_ID, ADMIN_ID), adminLunch);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void getWrongUser() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_LUNCH_ID, USER_ID));
    }

    @Test
    public void delete() {
        service.delete(ADMIN_LUNCH_ID, ADMIN_ID);
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_LUNCH_ID, ADMIN_ID));
    }

    @Test
    public void deleteNotFoundMeal() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, ADMIN_ID));
    }

    @Test
    public void deleteNotFoundUser() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_LUNCH_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusiveStartEnd() {
        LocalDate startDate = LocalDate.of(2020, Month.JANUARY, 31);
        LocalDate endDate = LocalDate.of(2020, Month.JANUARY, 31);
        assertMatch(service.getBetweenInclusive(startDate, endDate, USER_ID),
                getSortedListOf(userMealBorder31, userMealBreakfast31, userMealLunch31, userMealDinner31));
    }

    @Test
    public void getBetweenInclusiveEnd() {
        LocalDate endDate = LocalDate.of(2020, Month.JANUARY, 30);
        assertMatch(service.getBetweenInclusive(null, endDate, USER_ID),
                getSortedListOf(userMealBreakfast30, userMealLunch30, userMealDinner30));
    }

    @Test
    public void getBetweenInclusiveEmpty() {
        LocalDate startDateTime = LocalDate.of(2021, Month.JANUARY, 1);
        assertMatch(service.getBetweenInclusive(startDateTime, null, USER_ID), Collections.emptyList());
    }

    @Test
    public void getAll() {
        assertMatch(service.getAll(ADMIN_ID), adminDinner, adminLunch);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, ADMIN_ID);
        assertMatch(service.get(updated.getId(), ADMIN_ID), getUpdated());
    }

    @Test
    public void updateNotFound() {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () -> service.update(updated, USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), ADMIN_ID);
        Meal newMeal = getNew();
        newMeal.setId(created.getId());
        assertMatch(created, newMeal);
        assertMatch(service.get(created.getId(), ADMIN_ID), newMeal);
    }

    @Test
    public void createDuplicateKey() {
        service.create(getNew(), ADMIN_ID);
        assertThrows(DuplicateKeyException.class, () -> service.create(getNew(), ADMIN_ID));
    }
}