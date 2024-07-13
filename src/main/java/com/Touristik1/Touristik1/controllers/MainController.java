package com.Touristik1.Touristik1.controllers;

import com.Touristik1.Touristik1.Services.UserService;
import com.Touristik1.Touristik1.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/") //обрабатывает определенный UML адрес, / - главная страница
    public String home(Model model) { //вызываем определенный шаблон. передаем обязательный параметр Model
        // Получение пользователя
        User user = userService.getCurrentUser();

        // Установка пользователя в модель
        model.addAttribute("user", user);

        return "home"; //вызываем определенный шаблон по его названию
    }

    @GetMapping("/about")
    public String about(Model model) { //http://localhost:8080/about# - вот так можем перейти
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "about-us";
    }

    @GetMapping("/contact")
    public String contact(Model model) { //http://localhost:8080/about# - вот так можем перейти
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "contact";
    }
}
