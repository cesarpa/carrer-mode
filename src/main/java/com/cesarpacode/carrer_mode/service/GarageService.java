package com.cesarpacode.carrer_mode.service;

import com.cesarpacode.carrer_mode.model.*;
import com.cesarpacode.carrer_mode.repository.*;
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

    @Autowired
    private ChampionshipRepository championshipRepository;

    @Autowired
    private GarageChampionshipRepository garageChampionshipRepository;

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

    @Transactional
    public void joinChampionship(Long garageId, Long championshipId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid garage Id: " + garageId));
        Championship championship = championshipRepository.findById(championshipId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid championship Id: " + championshipId));

        // Check if already joined
        boolean exists = garage.getGarageChampionships().stream()
                .anyMatch(gc -> gc.getChampionship().getId().equals(championshipId));

        if (!exists) {
            // Check entry fee
            if (championship.getEntryFee() != null && garage.getCredits() < championship.getEntryFee()) {
                throw new IllegalStateException("Insufficient credits to join this championship. Required: $" + championship.getEntryFee());
            }

            // Deduct entry fee
            if (championship.getEntryFee() != null) {
                garage.setCredits(garage.getCredits() - championship.getEntryFee());
                garageRepository.save(garage);
            }

            GarageChampionship garageChampionship = new GarageChampionship();
            garageChampionship.setGarage(garage);
            garageChampionship.setChampionship(championship);
            garageChampionshipRepository.save(garageChampionship);
        }
    }
    
    @Transactional
    public void leaveChampionship(Long garageId, Long championshipId) {
        garageChampionshipRepository.findAll().stream()
                .filter(gc -> gc.getGarage().getId().equals(garageId) && gc.getChampionship().getId().equals(championshipId))
                .findFirst()
                .ifPresent(gc -> garageChampionshipRepository.delete(gc));
    }

    /**
     * Lógica de carrera para un vehículo individual.
     * Calcula FEE (1-5%), Pool (25-65%) y resultado según posición.
     */
    @Transactional
    public String processVehicleRace(Long garageId, Long carId, Integer placement) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid garage Id: " + garageId));
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid car Id: " + carId));

        // 1. Cálculo de FEE (1% a 5%)
        double entryPercent = 0.01 + (Math.random() * 0.04);
        long entryFee = (long) (car.getPrice() * entryPercent);

        if (garage.getCredits() < entryFee) {
            throw new IllegalStateException("Insufficient credits for entry fee: $" + entryFee);
        }

        // 2. Lógica de Pool (25% a 65%)
        double poolPercentage = 0.25 + (Math.random() * 0.40);
        long totalPool = (long) (car.getPrice() * poolPercentage);

        // 3. Posición (si no viene, aleatoria 1-12)
        int finalPos = (placement != null && placement > 0) ? placement : (int) (Math.random() * 12) + 1;

        // 4. Distribución de premios
        double winPercentage = 0;
        switch (finalPos) {
            case 1 -> winPercentage = 0.25;
            case 2 -> winPercentage = 0.18;
            case 3 -> winPercentage = 0.15;
            case 4 -> winPercentage = 0.12;
            case 5 -> winPercentage = 0.10;
            case 6 -> winPercentage = 0.08;
            case 7 -> winPercentage = 0.06;
            case 8 -> winPercentage = 0.04;
            case 9 -> winPercentage = 0.02;
            case 10 -> winPercentage = 0.01;
        }

        long winnings = (long) (totalPool * winPercentage);
        long netProfit = winnings - entryFee;

        garage.setCredits(garage.getCredits() + netProfit);
        garageRepository.save(garage);

        String resultType = netProfit >= 0 ? "Win" : "Loss";
        return String.format("Position: %dº | %s: $%d (Fee was $%d)", finalPos, resultType, Math.abs(netProfit), entryFee);
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