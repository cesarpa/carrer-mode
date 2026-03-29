package com.cesarpacode.carrer_mode.controller;

import com.cesarpacode.carrer_mode.model.Car;
import com.cesarpacode.carrer_mode.service.CarService;
import com.cesarpacode.carrer_mode.service.CategoryService;
import com.cesarpacode.carrer_mode.service.GarageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {
    @Autowired
    private CarService carService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private GarageService garageService;

    @GetMapping
    public String carList(Model model) {
        model.addAttribute("cars", carService.getAll());
        return "cars-list";
    }

    @GetMapping("/new")
    public String showAddCarForm(@RequestParam(required = false) Long garageId, Model model) {
        Car car = new Car();
        if (garageId != null) {
            car.setGarageId(garageId);
        }
        model.addAttribute("car", car);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("garages", garageService.getAll());
        return "car-form";
    }
    
    @PostMapping
    public String saveCar(@ModelAttribute Car car) {
        carService.create(car);
        return "redirect:/cars";
    }
    
    @GetMapping("/{id}/edit")
    public String showEditCarForm(@PathVariable Long id, Model model) {
        Car car = carService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + id));
        
        // Populate garageId from first associated garage if exists
        if (!car.getGarageCars().isEmpty()) {
            car.setGarageId(car.getGarageCars().get(0).getGarage().getId());
        }
        
        model.addAttribute("car", car);
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("garages", garageService.getAll());
        return "car-form";
    }
    
    @PostMapping("/{id}")
    public String updateCar(@PathVariable Long id, @ModelAttribute Car car) {
        carService.save(id, car);
        return "redirect:/cars";
    }
    
    @GetMapping("/{id}/delete")
    public String deleteCar(@PathVariable Long id) {
        carService.delete(id);
        return "redirect:/cars";
    }
    
    // REST API endpoints
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAll());
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.create(car));
    }
    
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Car> updateCarApi(@PathVariable Long id, @RequestBody Car car) {
        return carService.findById(id)
                .map(c -> ResponseEntity.ok(carService.save(id, car)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteCarApi(@PathVariable Long id) {
        if (carService.findById(id).isPresent()) {
            carService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
