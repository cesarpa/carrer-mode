package com.cesarpacode.carrer_mode.service;

import com.cesarpacode.carrer_mode.model.Category;
import com.cesarpacode.carrer_mode.model.Championship;
import com.cesarpacode.carrer_mode.model.ChampionshipCategory;
import com.cesarpacode.carrer_mode.repository.CategoryRepository;
import com.cesarpacode.carrer_mode.repository.ChampionshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChampionshipService {

    @Autowired
    private ChampionshipRepository championshipRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Championship> getAll() {
        return championshipRepository.findAll();
    }

    public Optional<Championship> findById(Long id) {
        return championshipRepository.findById(id).map(championship -> {
            if (championship.getChampionshipCategories() != null) {
                championship.setCategoryIds(championship.getChampionshipCategories().stream()
                        .map(cc -> cc.getCategory().getId())
                        .collect(Collectors.toList()));
            }
            return championship;
        });
    }

    public Championship create(Championship championship) {
        updateCategories(championship);
        return championshipRepository.save(championship);
    }

    public Championship save(Long id, Championship championship) {
        championship.setId(id);
        updateCategories(championship);
        return championshipRepository.save(championship);
    }

    private void updateCategories(Championship championship) {
        if (championship.getCategoryIds() != null) {
            List<ChampionshipCategory> categories = new ArrayList<>();
            for (Long categoryId : championship.getCategoryIds()) {
                Category category = categoryRepository.findById(categoryId).orElse(null);
                if (category != null) {
                    ChampionshipCategory cc = new ChampionshipCategory();
                    cc.setChampionship(championship);
                    cc.setCategory(category);
                    categories.add(cc);
                }
            }
            // Clear and add all to handle orphan removal correctly
            if (championship.getChampionshipCategories() == null) {
                championship.setChampionshipCategories(new ArrayList<>());
            } else {
                championship.getChampionshipCategories().clear();
            }
            championship.getChampionshipCategories().addAll(categories);
        }
    }

    public void delete(Long id) {
        championshipRepository.deleteById(id);
    }

    public long calculateSpecialWin(Long championshipId, int position) {
        return championshipRepository.findById(championshipId)
                .map(championship -> {
                    double percentage = getPercentageByPlacement(position);
                    return (long) (championship.getTotalPrize() * percentage);
                })
                .orElse(0L);
    }

    public long calculateEarnings(Long championshipId, int placement) {
        return championshipRepository.findById(championshipId)
                .map(championship -> {
                    double percentage = getPercentageByPlacement(placement);
                    return (long) (championship.getTotalPrize() * percentage);
                })
                .orElse(0L);
    }

    private double getPercentageByPlacement(int placement) {
        return switch (placement) {
            case 1 -> 1.0;
            case 2 -> 0.60;
            case 3 -> 0.40;
            case 4 -> 0.24;
            case 5 -> 0.16;
            case 6 -> 0.12;
            case 7 -> 0.10;
            case 8 -> 0.08;
            case 9 -> 0.06;
            case 10 -> 0.04;
            default -> 0.0;
        };
    }
}
