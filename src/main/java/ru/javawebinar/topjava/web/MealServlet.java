package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.GenericDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.PropUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String LIST_FORWARD = "meals.jsp";
    private static final String NEW_EDIT_FORWARD = "editmeal.jsp";
    private static final String LIST_REDIRECT = "./meals";
    private static final Logger log = getLogger(MealServlet.class);
    private static GenericDao<Meal> dao;


    @Override
    public void init() {
        try {
            dao = (GenericDao<Meal>) PropUtil.getDao();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);

        req.setAttribute("meals", mealsTo);

        log.debug("redirect to " + LIST_FORWARD);

        req.getRequestDispatcher(LIST_FORWARD).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String operation = req.getParameter("buttonPress");
        int id;
        String idVal = req.getParameter("id");
        log.debug(operation + " on " + (idVal.equals("") ? "new meal" : "meal.id=" + idVal));

        switch (operation) {
            case "mealDelete":
                id = Integer.parseInt(idVal);
                dao.delete(id);
                resp.sendRedirect(LIST_REDIRECT);
                break;
            case "mealEdit":
                if (!idVal.equals("")) {
                    try {
                        id = Integer.parseInt(req.getParameter("id"));
                        Meal meal = dao.getById(id);
                        req.setAttribute("id", id);
                        req.setAttribute("dateTime", meal.getDateTime());
                        req.setAttribute("description", meal.getDescription());
                        req.setAttribute("calories", meal.getCalories());
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("redirect to " + NEW_EDIT_FORWARD);
                req.getRequestDispatcher(NEW_EDIT_FORWARD).forward(req, resp);
                break;
            case "editMealCancel":
                resp.sendRedirect(LIST_REDIRECT);
                break;
            case "editMealSubmit":
                log.debug(operation + " on meal.id=" + idVal);
                if (idVal.equals("")) {
                    dao.add(new Meal(LocalDateTime.parse(req.getParameter("dateTime"), TimeUtil.getFormatter()),
                            req.getParameter("description"),
                            Integer.parseInt(req.getParameter("calories") != null ? req.getParameter("calories") : "0")));
                } else {
                    dao.update(new Meal(Integer.parseInt(idVal),
                            LocalDateTime.parse(req.getParameter("dateTime"), TimeUtil.getFormatter()),
                            req.getParameter("description"),
                            Integer.parseInt(req.getParameter("calories") != null ? req.getParameter("calories") : "0")));
                }
                resp.sendRedirect(LIST_REDIRECT);
                break;
        }
    }
}
