package com.cesarpacode.carrer_mode.controller;

import com.cesarpacode.carrer_mode.service.CarService;
import com.cesarpacode.carrer_mode.service.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {
    @Autowired
    private CarService carService;

    @Autowired
    private GarageService garageService;
    
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("cars", carService.getAll());
        model.addAttribute("garages", garageService.getAll());
        return "menu";
    }

}
