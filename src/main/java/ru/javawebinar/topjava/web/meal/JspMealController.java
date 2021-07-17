package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Controller
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("/meals")
    public String getAll(Model model) {
        model.addAttribute("meals",
                super.getAll());
        return "meals";
    }

    @GetMapping("/meals/delete?id={id}")
    public String deleteOne(@PathVariable(value = "id") int id) {
        super.delete(id);
        return "redirect:/meals";
    }

    @GetMapping({"/meals/update", "/meals/create"})
    public String openEditForm(@RequestParam(required = false) Integer id, Model model) {
        int userId = SecurityUtil.authUserId();
        Meal meal;
        if (id == null) {
            log.info("openEditForm for create new meal");
            meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        } else {
            log.info("openEditForm for meal {}", id);
            meal = service.get(id, userId);
        }
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping("/meals")
    public String save(@RequestParam("dateTime")
                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                       @RequestParam("description") String description,
                       @RequestParam("calories") int calories,
                       @RequestParam(name = "id", required = false) Integer id) {
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(dateTime, description, calories);
        if (id != null) {
            meal.setId(id);
            service.update(meal, userId);
        } else {
            service.create(meal, userId);
        }
        return "redirect:/meals";
    }

    @GetMapping("/meals/filter")
    public String filter(@RequestParam(required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                         @RequestParam(required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                         @RequestParam(required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
                         @RequestParam(required = false)
                         @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
                         Model model) {
        model.addAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}