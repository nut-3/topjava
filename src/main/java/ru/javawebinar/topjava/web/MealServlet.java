package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String LIST_FORWARD = "meals.jsp";
    private static final String NEW_EDIT_FORWARD = "editmeal.jsp";
    private static final String LIST_REDIRECT = "./meals";
    private static final Logger log = getLogger(MealServlet.class);
    private static Dao<Meal> dao;

    @Override
    public void init() {
        dao = new InMemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");

        String operation = Optional.ofNullable(req.getParameter("action")).orElse("");

        switch (operation) {
            case "cancel":
                log.debug("redirect to {}", LIST_REDIRECT);
                resp.sendRedirect(LIST_REDIRECT);
                break;
            case "new":
            case "edit":
                String idVal = req.getParameter("id");
                try {
                    int id = Integer.parseInt(idVal);
                    dao.getById(id).ifPresent(meal -> req.setAttribute("meal", meal));
                } catch (NumberFormatException ignored) { }

                log.debug("forward to {}", NEW_EDIT_FORWARD);
                req.getRequestDispatcher(NEW_EDIT_FORWARD).forward(req, resp);
                break;
            default:
                List<MealTo> mealsTo = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
                req.setAttribute("meals", mealsTo);

                log.debug("forward to {}", LIST_FORWARD);

                req.getRequestDispatcher(LIST_FORWARD).forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String operation = Optional.ofNullable(req.getParameter("button")).orElse("");
        String idVal = req.getParameter("id");

        log.debug("operation {} on {}", operation, (idVal.equals("") ? "new meal" : "meal.id=" + idVal));

        switch (operation) {
            case "delete":
                try {
                    int id = Integer.parseInt(idVal);
                    dao.delete(id);
                } catch (NumberFormatException e) {
                    log.debug("wrong id value {}", idVal);
                    e.printStackTrace();
                }
                resp.sendRedirect(LIST_REDIRECT);
                break;
            case "save":
                LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"), TimeUtil.CLASS_FORMATTER);
                String description = req.getParameter("description");
                int calories = Integer.parseInt(Optional.ofNullable(req.getParameter("calories")).orElse("0"));

                try {
                    int id = Integer.parseInt(idVal);
                    dao.update(new Meal(id, dateTime, description, calories));
                } catch (NumberFormatException e) {
                    dao.add(new Meal(dateTime, description, calories));
                }

                resp.sendRedirect(LIST_REDIRECT);
                break;
        }
    }
}
