package com.cesarpacode.carrer_mode.controller;

import com.cesarpacode.carrer_mode.model.Garage;
import com.cesarpacode.carrer_mode.service.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/garages")
public class GarageController {
    
    @Autowired
    private GarageService garageService;
    
    @GetMapping
    public String listGarages(Model model) {
        model.addAttribute("garages", garageService.getAll());
        return "garages-list";
    }
    
    @GetMapping("/new")
    public String showCreateGarageForm(Model model) {
        model.addAttribute("garage", new Garage());
        return "garage-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditGarageForm(@PathVariable Long id, Model model) {
        Garage garage = garageService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid garage Id:" + id));
        model.addAttribute("garage", garage);
        return "garage-form";
    }

    @GetMapping("/{id}")
    public String viewGarage(@PathVariable Long id, Model model) {
        Garage garage = garageService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid garage Id:" + id));
        model.addAttribute("garage", garage);
        return "current-garage";
    }
    
    @PostMapping
    public String saveGarage(@ModelAttribute Garage garage) {
        garageService.save(garage.getId(), garage);
        return "redirect:/garages";
    }

    @PostMapping("/delete/{id}")
    public String deleteGarage(@PathVariable Long id) {
        garageService.delete(id);
        return "redirect:/garages";
    }
}
