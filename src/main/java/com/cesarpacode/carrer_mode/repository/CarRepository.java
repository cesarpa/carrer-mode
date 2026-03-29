package com.cesarpacode.carrer_mode.repository;

import com.cesarpacode.carrer_mode.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    public Car findByName(String name);
}