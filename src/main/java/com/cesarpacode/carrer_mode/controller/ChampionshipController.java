package com.cesarpacode.carrer_mode.controller;

import com.cesarpacode.carrer_mode.model.Championship;
import com.cesarpacode.carrer_mode.service.ChampionshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/championships")
public class ChampionshipController {

    @Autowired
    private ChampionshipService championshipService;

    @GetMapping
    public ResponseEntity<List<Championship>> getAll() {
        return ResponseEntity.ok(championshipService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Championship> getById(@PathVariable Long id) {
        return championshipService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Championship> create(@RequestBody Championship championship) {
        return ResponseEntity.status(HttpStatus.CREATED).body(championshipService.create(championship));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Championship> update(@PathVariable Long id, @RequestBody Championship championship) {
        return championshipService.findById(id)
                .map(c -> ResponseEntity.ok(championshipService.save(id, championship)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (championshipService.findById(id).isPresent()) {
            championshipService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/calculate-earnings/{placement}")
    public ResponseEntity<Long> calculateEarnings(@PathVariable Long id, @PathVariable int placement) {
        if (championshipService.findById(id).isPresent()) {
            return ResponseEntity.ok(championshipService.calculateEarnings(id, placement));
        }
        return ResponseEntity.notFound().build();
    }
}
