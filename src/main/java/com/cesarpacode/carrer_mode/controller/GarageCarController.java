package com.cesarpacode.carrer_mode.controller;

import com.cesarpacode.carrer_mode.model.Garage;
import com.cesarpacode.carrer_mode.service.GarageService;
import com.cesarpacode.carrer_mode.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/race-result")
    @ResponseBody // Retorna JSON en lugar de una página
    public ResponseEntity<?> processRaceResult(@RequestBody Map<String, Object> payload) {
        Long garageId = Long.valueOf(payload.get("garageId").toString());
        Double amount = Double.valueOf(payload.get("amount").toString()); // El netProfit (positivo o negativo)

        // Actualizamos el saldo en el servicio
        Garage updatedGarage = garageService.updateBalance(garageId, amount);

        // Devolvemos el nuevo saldo para que el JS lo pinte
        Map<String, Object> response = new HashMap<>();
        response.put("newBalance", updatedGarage.getCredits());
        return ResponseEntity.ok(response);
    }
}
