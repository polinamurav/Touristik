package com.Touristik1.Touristik1.controllers;

import com.Touristik1.Touristik1.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final UserService userService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userService.list()); //передаем всех пользователей
        return "admin";
    }

    @PostMapping("/admin-user-ban")
    public String userBan(@RequestParam("id") Long id) {
        userService.banUser(id);
        return "redirect:/admin";
    }

}
