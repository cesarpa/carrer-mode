package com.cesarpacode.carrer_mode.controller;

import com.cesarpacode.carrer_mode.model.Garage;
import com.cesarpacode.carrer_mode.service.GarageService;
import com.cesarpacode.carrer_mode.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/garage-cars")
public class GarageCarController {

    @Autowired
    private GarageService garageService;

    @Autowired
    private CarService carService;

    @GetMapping("/assign")
    public String showAssignCarForm(@RequestParam Long garageId, Model model) {
        Garage garage = garageService.getById(garageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid garage Id:" + garageId));
        model.addAttribute("garage", garage);
        model.addAttribute("cars", carService.getAll());
        return "garage-car-form";
    }

    @PostMapping("/assign")
    public String assignCar(@RequestParam Long garageId, @RequestParam Long carId, Model model) {
        try {
            garageService.addCarToGarage(garageId, carId);
            return "redirect:/garages/" + garageId;
        } catch (IllegalStateException e) {
            Garage garage = garageService.getById(garageId).orElseThrow();
            model.addAttribute("garage", garage);
            model.addAttribute("cars", carService.getAll());
            model.addAttribute("error", e.getMessage());
            return "garage-car-form";
        }
    }

    @PostMapping("/sell")
    public String sellCar(@RequestParam Long garageId, @RequestParam Long carId) {
        garageService.sellCarFromGarage(garageId, carId);
        return "redirect:/garages/" + garageId;
    }

    @PostMapping("/race")
    public String race(@RequestParam Long garageId, @RequestParam Long carId, 
                       @RequestParam(required = false) Integer placement,
                       RedirectAttributes redirectAttributes) {
        try {
            String result = garageService.processVehicleRace(garageId, carId, placement);
            redirectAttributes.addFlashAttribute("raceResult", result);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/garages/" + garageId;
    }
}
