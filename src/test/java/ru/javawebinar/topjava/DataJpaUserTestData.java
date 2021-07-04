package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.User;

public class DataJpaUserTestData extends UserTestData {
    public static final MatcherFactory<User> MATCHER = MatcherFactory.usingIgnoringFieldsComparator("registered", "roles");
}
