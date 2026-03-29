package com.cesarpacode.carrer_mode.repository;

import com.cesarpacode.carrer_mode.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  }
