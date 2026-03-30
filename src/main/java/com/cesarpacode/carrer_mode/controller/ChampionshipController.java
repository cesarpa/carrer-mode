package com.cesarpacode.carrer_mode.controller;

import com.cesarpacode.carrer_mode.model.Championship;
import com.cesarpacode.carrer_mode.service.CategoryService;
import com.cesarpacode.carrer_mode.service.ChampionshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/championships")
public class ChampionshipController {

    @Autowired
    private ChampionshipService championshipService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String championshipList(Model model) {
        model.addAttribute("championships", championshipService.getAll());
        return "championships-list";
    }

    @GetMapping("/new")
    public String showAddChampionshipForm(Model model) {
        model.addAttribute("championship", new Championship());
        model.addAttribute("categories", categoryService.getAll());
        return "championship-form";
    }

    @PostMapping
    public String saveChampionship(@ModelAttribute Championship championship) {
        championshipService.create(championship);
        return "redirect:/championships";
    }

    @GetMapping("/{id}/edit")
    public String showEditChampionshipForm(@PathVariable Long id, Model model) {
        Championship championship = championshipService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid championship Id: " + id));
        model.addAttribute("championship", championship);
        model.addAttribute("categories", categoryService.getAll());
        return "championship-form";
    }

    @PostMapping("/{id}")
    public String updateChampionship(@PathVariable Long id, @ModelAttribute Championship championship) {
        championshipService.save(id, championship);
        return "redirect:/championships";
    }

    @GetMapping("/{id}/delete")
    public String deleteChampionship(@PathVariable Long id) {
        championshipService.delete(id);
        return "redirect:/championships";
    }

    // REST API endpoints
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Championship>> getAll() {
        return ResponseEntity.ok(championshipService.getAll());
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Championship> getById(@PathVariable Long id) {
        return championshipService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Championship> create(@RequestBody Championship championship) {
        return ResponseEntity.status(HttpStatus.CREATED).body(championshipService.create(championship));
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Championship> update(@PathVariable Long id, @RequestBody Championship championship) {
        return championshipService.findById(id)
                .map(c -> ResponseEntity.ok(championshipService.save(id, championship)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (championshipService.findById(id).isPresent()) {
            championshipService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
