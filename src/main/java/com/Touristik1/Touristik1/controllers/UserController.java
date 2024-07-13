package com.Touristik1.Touristik1.controllers;

import com.Touristik1.Touristik1.Services.TourService;
import com.Touristik1.Touristik1.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import com.Touristik1.Touristik1.Services.UserService;
import com.Touristik1.Touristik1.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TourService tourService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }



    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("name", new User());
        return "registration";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/tour";
    }


    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        userService.createUser(user);
        return "redirect:/login";
    }


    //страница с заказами
    @GetMapping("/orders")
    public String orders(Model model, Principal principal) {
        if (principal != null) {
            User user = tourService.getUserByPrincipal(principal);
            model.addAttribute("user", user);

            // Передаем туры пользователя в модель
            model.addAttribute("tours", user.getTours());
        }

        return "basket-user";
    }

    @PostMapping("/tour/choose")
    public String addToCart(@RequestParam("tourId") Long tourId, Principal principal) {
        if (principal != null) {
            User user = userService.getCurrentUser();
            Tour tour = tourService.getTourById(tourId);

            if (tour != null) {
                tour.setUser(user); //с текущим пользователем
                user.getTours().add(tour);
                userService.saveUser(user);
            }
        }

        return "redirect:/orders";
    }


}
