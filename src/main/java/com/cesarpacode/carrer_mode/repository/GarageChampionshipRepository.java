package com.cesarpacode.carrer_mode.repository;

import com.cesarpacode.carrer_mode.model.GarageChampionship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarageChampionshipRepository extends JpaRepository<GarageChampionship, Long> {
}
