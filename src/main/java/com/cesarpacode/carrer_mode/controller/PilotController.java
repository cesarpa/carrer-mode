package com.cesarpacode.carrer_mode.controller;

import com.cesarpacode.carrer_mode.model.Pilot;
import com.cesarpacode.carrer_mode.service.PilotService;
import com.cesarpacode.carrer_mode.service.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pilots")
public class PilotController {
    
    @Autowired
    private PilotService pilotService;
    
    @Autowired
    private GarageService garageService;

    @GetMapping
    public String listPilots(Model model) {
        model.addAttribute("pilots", pilotService.getAll());
        return "pilots-list";
    }

    @GetMapping("/new")
    public String showAddPilotForm(Model model) {
        Pilot pilot = new Pilot();
        pilot.setGarage(new com.cesarpacode.carrer_mode.model.Garage());
        model.addAttribute("pilot", pilot);
        model.addAttribute("garages", garageService.getAll());
        return "pilot-form";
    }

    @PostMapping
    public String savePilot(@ModelAttribute Pilot pilot) {
        // If garage id is null, set garage to null to avoid saving an empty garage object
        if (pilot.getGarage() != null && pilot.getGarage().getId() == null) {
            pilot.setGarage(null);
        }
        
        if (pilot.getId() == null) {
            pilotService.create(pilot);
        } else {
            pilotService.save(pilot.getId(), pilot);
        }
        return "redirect:/pilots";
    }

    @GetMapping("/edit/{id}")
    public String showEditPilotForm(@PathVariable Long id, Model model) {
        Pilot pilot = pilotService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pilot Id:" + id));
        if (pilot.getGarage() == null) {
            pilot.setGarage(new com.cesarpacode.carrer_mode.model.Garage());
        }
        model.addAttribute("pilot", pilot);
        model.addAttribute("garages", garageService.getAll());
        return "pilot-form";
    }

    @GetMapping("/{id}")
    public String viewPilot(@PathVariable Long id, Model model) {
        Pilot pilot = pilotService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pilot Id:" + id));
        model.addAttribute("pilot", pilot);
        return "current-pilot";
    }

    @GetMapping("/delete/{id}")
    public String deletePilot(@PathVariable Long id) {
        pilotService.delete(id);
        return "redirect:/pilots";
    }
}
