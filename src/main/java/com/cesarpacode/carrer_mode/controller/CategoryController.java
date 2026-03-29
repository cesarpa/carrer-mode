package com.cesarpacode.carrer_mode.controller;

import com.cesarpacode.carrer_mode.model.Category;
import com.cesarpacode.carrer_mode.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        return "categories-list";
    }
    
    @GetMapping("/new")
    public String showCreateCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-form";
    }
    
    @PostMapping
    public String createCategory(@ModelAttribute Category category) {
        categoryService.save(null, category);
        return "redirect:/categories/success";
    }
    
    @GetMapping("/success")
    public String showSuccessPage() {
        return "category-success";
    }
}
