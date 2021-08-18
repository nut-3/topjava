package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "meals", "password");
    public static MatcherFactory.Matcher<User> WITH_MEALS_MATCHER =
            MatcherFactory.usingAssertions(User.class,
//     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                            .ignoringFields("registered", "meals.user", "password").isEqualTo(e),
                    (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int NOT_FOUND = 10;

    public static final User user = new User(USER_ID, "User", "user@yandex.ru", "password", 2005, Role.USER);
    public static final User admin = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", 1900, Role.ADMIN, Role.USER);

    public static final String jsonWithErrorEmpty = """
            {
                "name": "",
                "email": "",
                "password": "",
                "roles": []
            }""";
    public static final String updatedJsonWithErrorInvalid = """
            {
                "name": "UserUpdated",
                "email": "user@yandex.ru",
                "password": "passwordNew",
                "roles":
            }""";
    public static final String updatedJsonWithErrorInvalidEmail = """
            {
                "name": "UserUpdated",
                "email": "user_yandex.ru",
                "password": "passwordNew",
                "roles": [
                    "USER"
                ]
            }""";
    public static final String newJsonWithErrorInvalid = """
            {
                "name": "New2",
                "email": "new2@yandex.ru",
                "password": "passwordNew",
                "roles":
            }""";
    public static final String newJsonWithErrorInvalidEmail = """
            {
                "name": "New2",
                "email": "new2_yandex.ru",
                "password": "passwordNew",
                "roles": [
                    "USER"
                ]
            }""";
    public static final String registerJsonWithErrorEmpty = """
            {
                "name": "",
                "email": "",
                "password": ""
            }""";
    public static final String registerJsonWithErrorUnprocessed = """
            {
                "name": "New2",
                "email": "new2@yandex.ru",
                "password":
            }""";
    public static final String registerJsonWithErrorEmail = """
            {
                "name": "New2",
                "email": "new2_yandex.ru",
                "password": "passwordNew"
            }""";

    static {
        user.setMeals(meals);
        admin.setMeals(List.of(adminMeal2, adminMeal1));
    }

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
