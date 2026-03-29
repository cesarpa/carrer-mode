package com.cesarpacode.carrer_mode.repository;

import com.cesarpacode.carrer_mode.model.Championship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionshipRepository extends JpaRepository<Championship, Long> {
}
