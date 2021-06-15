package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName2", "email@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName2", "email2@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName1", "email1@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName3", "email3@mail.ru", "password", Role.ADMIN));
            adminUserController.getAll().forEach(System.out::println);
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            System.out.println();
            SecurityUtil.setUserId(2);
            mealRestController.get(7);
        }
    }
}
