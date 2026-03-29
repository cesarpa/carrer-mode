package com.cesarpacode.carrer_mode.repository;

import com.cesarpacode.carrer_mode.model.GarageCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarageCarRepository extends JpaRepository<GarageCar, Long> {
    List<GarageCar> findByCarId(Long carId);
    List<GarageCar> findByGarageId(Long garageId);
    void deleteByCarIdAndGarageId(Long carId, Long garageId);
}
