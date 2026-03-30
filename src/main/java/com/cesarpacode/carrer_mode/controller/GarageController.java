package com.cesarpacode.carrer_mode.controller;

import com.cesarpacode.carrer_mode.model.Garage;
import com.cesarpacode.carrer_mode.service.ChampionshipService;
import com.cesarpacode.carrer_mode.service.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/garages")
public class GarageController {
    
    @Autowired
    private GarageService garageService;

    @Autowired
    private ChampionshipService championshipService;
    
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
        model.addAttribute("allChampionships", championshipService.getAll());
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

    @PostMapping("/{id}/join-championship")
    public String joinChampionship(@PathVariable Long id, @RequestParam Long championshipId, RedirectAttributes redirectAttributes) {
        try {
            garageService.joinChampionship(id, championshipId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/garages/" + id;
    }

    @PostMapping("/{id}/complete-championship")
    public String completeChampionship(@PathVariable Long id, 
                                                @RequestParam Long championshipId, 
                                                @RequestParam int position,
                                                RedirectAttributes redirectAttributes) {
        try {
            long prize = championshipService.calculateSpecialWin(championshipId, position);
            garageService.updateBalance(id, (double) prize);
            garageService.leaveChampionship(id, championshipId);
            redirectAttributes.addFlashAttribute("raceResult", "Championship Race Completed! Position: " + position + "º | Prize: $" + prize);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/garages/" + id;
    }
}
