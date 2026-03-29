package com.cesarpacode.carrer_mode.repository;

import com.cesarpacode.carrer_mode.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {
    Garage findByName(String name);
}
