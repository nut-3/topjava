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
import java.util.*;

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

        String operation = getActionName(req);
        String idVal = getRequestParamValue(operation, req);

        switch (operation) {
            case "cancel":
                log.debug("redirect to " + LIST_REDIRECT);
                resp.sendRedirect(LIST_REDIRECT);
                break;
            case "edit":
                if (!idVal.equals("")) {
                    int id = Integer.parseInt(idVal);
                    Meal meal;
                    Optional<Meal> optMeal = dao.getById(id);
                    if (optMeal.isPresent()) {
                        meal = optMeal.get();
                        req.setAttribute("meal", meal);
                    }
                }

                log.debug("forward to " + NEW_EDIT_FORWARD);
                req.getRequestDispatcher(NEW_EDIT_FORWARD).forward(req, resp);
                break;
            default:
                List<MealTo> mealsTo = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
                req.setAttribute("meals", mealsTo);

                log.debug("forward to " + LIST_FORWARD);

                req.getRequestDispatcher(LIST_FORWARD).forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String operation = getRequestParamValue("button", req);
        String idVal = getRequestParamValue("id", req);

        log.debug(operation + " on " + (idVal.equals("") ? "new meal" : "meal.id=" + idVal));

        switch (operation) {
            case "delete":
                int id = Integer.parseInt(idVal);
                dao.delete(id);
                resp.sendRedirect(LIST_REDIRECT);
                break;
            case "save":
                LocalDateTime dateTime = LocalDateTime.parse(getRequestParamValue("dateTime", req), TimeUtil.CLASS_FORMATTER);
                String description = getRequestParamValue("description", req);
                int calories = Integer.parseInt(getRequestParamValue("calories", req));

                if (idVal.equals("")) {
                    dao.add(new Meal(dateTime, description, calories));
                } else {
                    id = Integer.parseInt(idVal);
                    dao.update(new Meal(id, dateTime, description, calories));
                }

                resp.sendRedirect(LIST_REDIRECT);
                break;
        }
    }

    private String getActionName(HttpServletRequest req) {
        List<String> actionList = Arrays.asList("edit", "cancel");

        Enumeration<String> reqParams = req.getParameterNames();
        while (reqParams.hasMoreElements()) {
            String action = reqParams.nextElement();
            if (actionList.contains(action))
                return action;
        }
        return "";
    }

    private String getRequestParamValue(String paramName, HttpServletRequest req) {
        Enumeration<String> reqParams = req.getParameterNames();

        while (reqParams.hasMoreElements()) {
            if (Objects.equals(reqParams.nextElement(), paramName)) {
                String param = req.getParameter(paramName);
                return (param != null ? param : "");
            }
        }
        return "";
    }
}
