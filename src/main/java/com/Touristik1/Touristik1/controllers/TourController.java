package com.Touristik1.Touristik1.controllers;

import com.Touristik1.Touristik1.Services.UserService;
import com.Touristik1.Touristik1.Tour;
import com.Touristik1.Touristik1.Services.TourService;
import com.Touristik1.Touristik1.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class TourController {

    private final TourService tourService;
    private final UserService userService;


    @Autowired
    public TourController(TourService tourService, UserService userService) {
        this.tourService = tourService;
        this.userService = userService;
    }

    //все путевки для пользователя, а также поиск
    @GetMapping("/tour") //чтобы отслеживать UML адреса
    public String tourAll(@RequestParam(name = "title", required = false) String title, Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("tourList", tourService.listTours(title));
        return "tours/tour-my-bd";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    //все путевки для админа
    @GetMapping("/tour-admin") //чтобы отслеживать UML адреса
    public String tourAllAdmin(@RequestParam(name = "title", required = false) String title, Model model) {
        model.addAttribute("tourList", tourService.listTours(title));
        return "tours/tour-main-admin";
    }

    //добавление путевки
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/tour-admin/add")
    public String newTour(Model model) {
        model.addAttribute("tour", new Tour());
        return "tours/tour-add";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/tour")
    public String create(@ModelAttribute("tour") Tour tour, @RequestParam("file") MultipartFile file,
                         Principal principal) throws IOException {
        tourService.saveTours(principal, tour, file);
        return "redirect:/tour-admin"; //redirect - переход на другую страницу после добавления тура
    }

    //изменение путевки
    //сначала находите тур и добавляете его в модель, а затем переходите на страницу редактирования
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/tour/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        Tour tour = tourService.getTourById(id);
        model.addAttribute("tour", tour);
        return "tours/tour-edit";
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/edit/{id}")
    public String updateTour(@ModelAttribute("tour") Tour tour, @PathVariable("id") Long id,
                             @RequestParam("file") MultipartFile file) throws IOException {
        tourService.updateTour(tour, id, file);
        return "redirect:/tour-admin";
    }


    //удаление путевки
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/delete-tour")
    public String deleteTour(@RequestParam("id") Long id) {
        // Удаление тура
        tourService.deleteTour(id);
        return "redirect:/tour-admin"; // Указываем адрес страницы успеха
    }

    //сортировка путевки
    @PostMapping("/sort-tour-asc")
    public String sortTours(Model model) {
        List<Tour> sortedTours = tourService.sortTours(Sort.Direction.ASC);
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("tourList", sortedTours);
        return "tours/tour-my-bd";
    }


}
