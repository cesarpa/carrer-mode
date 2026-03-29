package com.cesarpacode.carrer_mode.service;

import com.cesarpacode.carrer_mode.model.Car;
import com.cesarpacode.carrer_mode.model.Category;
import com.cesarpacode.carrer_mode.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }


    public Category save(Long id, Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }
    
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
