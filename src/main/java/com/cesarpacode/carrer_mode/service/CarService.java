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
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private GarageRepository garageRepository;

    @Autowired
    private GarageCarRepository garageCarRepository;

    public List<Car> getAll() {
        return carRepository.findAll();
    }
    
    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    public Car findByName(String name) {
        return carRepository.findByName(name);
    }
    
    @Transactional
    public Car create(Car car) {
        Car savedCar = carRepository.save(car);
        if (car.getGarageId() != null) {
            assignToGarage(savedCar, car.getGarageId());
        }
        return savedCar;
    }
    
    @Transactional
    public Car save(Long id, Car car) {
        car.setId(id);
        Car savedCar = carRepository.save(car);
        if (car.getGarageId() != null) {
            assignToGarage(savedCar, car.getGarageId());
        }
        return savedCar;
    }

    private void assignToGarage(Car car, Long garageId) {
        garageRepository.findById(garageId).ifPresent(garage -> {
            // Check if already assigned
            boolean alreadyAssigned = garageCarRepository.findAll().stream()
                    .anyMatch(gc -> gc.getCar().getId().equals(car.getId()) && gc.getGarage().getId().equals(garageId));
            
            if (!alreadyAssigned) {
                GarageCar garageCar = new GarageCar();
                garageCar.setCar(car);
                garageCar.setGarage(garage);
                garageCarRepository.save(garageCar);
            }
        });
    }
    
    public void delete(Long id) {
        carRepository.deleteById(id);
    }
}
