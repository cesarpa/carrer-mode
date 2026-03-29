package com.cesarpacode.carrer_mode.service;

import com.cesarpacode.carrer_mode.model.Car;
import com.cesarpacode.carrer_mode.model.Garage;
import com.cesarpacode.carrer_mode.model.GarageCar;
import com.cesarpacode.carrer_mode.repository.CarRepository;
import com.cesarpacode.carrer_mode.repository.GarageCarRepository;
import com.cesarpacode.carrer_mode.repository.GarageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GarageService {
    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private GarageCarRepository garageCarRepository;

    public List<Garage> getAll() {
        return garageRepository.findAll();
    }
    
    public Optional<Garage> getById(Long id) {
        return garageRepository.findById(id);
    }
    
    public Garage save(Long id, Garage garage) {
        if(id!= null) {
            Garage garageRepo = garageRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid garage Id: " + id));
            if (garageRepo != null) {
                garageRepo.setName(garage.getName());
                garageRepo.setCredits(garage.getCredits());
                garage = garageRepo;
            }
        }else{
            garage.setId(id);
        }
        return garageRepository.save(garage);
    }
    
    @Transactional
    public void addCarToGarage(Long garageId, Long carId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid garage Id: " + garageId));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + carId));
        
        // Check if association already exists
        boolean exists = garageCarRepository.findAll().stream()
                .anyMatch(gc -> gc.getCar().getId().equals(carId) && gc.getGarage().getId().equals(garageId));
        
        if (!exists) {
            // Check if enough credits
            if (garage.getCredits() < car.getPrice()) {
                throw new IllegalStateException("Insufficient credits to buy this vehicle. Required: $" + car.getPrice() + ", Available: $" + garage.getCredits());
            }

            // Deduct credits
            garage.setCredits(garage.getCredits() - car.getPrice());
            garageRepository.save(garage);

            GarageCar garageCar = new GarageCar();
            garageCar.setGarage(garage);
            garageCar.setCar(car);
            garageCarRepository.save(garageCar);
        }
    }

    @Transactional
    public void sellCarFromGarage(Long garageId, Long carId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid garage Id: " + garageId));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + carId));

        // Calculate sale price (70% of car price)
        long salePrice = (long) (car.getPrice() * 0.7);
        garage.setCredits(garage.getCredits() + salePrice);
        garageRepository.save(garage);

        // Remove from garage
        garageCarRepository.deleteByCarIdAndGarageId(carId, garageId);
    }
    
    public void delete(Long id) {
        garageRepository.deleteById(id);
    }

    /**
     * Actualiza el saldo del garaje de forma atómica.
     * @param garageId ID del garaje a modificar.
     * @param amount Cantidad a sumar (ganancia) o restar (pérdida/entry fee).
     * @return El garaje actualizado.
     */
    @Transactional
    public Garage updateBalance(Long garageId, Double amount) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid garage Id: " + garageId));

        // Sumamos el profit neto (si es negativo, restará automáticamente)
        Long newCredits = (long) (garage.getCredits() + amount);

        // Validación de seguridad: no permitir créditos negativos
        if (newCredits < 0) {
            throw new IllegalStateException("Balance cannot be negative. Current: " + garage.getCredits());
        }

        garage.setCredits(newCredits);
        return garageRepository.save(garage);
    }
}