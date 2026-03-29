package com.cesarpacode.carrer_mode.repository;

import com.cesarpacode.carrer_mode.model.ChampionshipCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChampionshipCategoryRepository extends JpaRepository<ChampionshipCategory, Long> {
}
