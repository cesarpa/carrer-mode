package com.cesarpacode.carrer_mode.repository;

import com.cesarpacode.carrer_mode.model.Pilot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PilotRepository extends JpaRepository<Pilot, Long> {

}