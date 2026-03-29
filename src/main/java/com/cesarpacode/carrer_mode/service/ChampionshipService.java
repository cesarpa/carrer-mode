package com.cesarpacode.carrer_mode.service;

import com.cesarpacode.carrer_mode.model.Championship;
import com.cesarpacode.carrer_mode.repository.ChampionshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChampionshipService {

    @Autowired
    private ChampionshipRepository championshipRepository;

    public List<Championship> getAll() {
        return championshipRepository.findAll();
    }

    public Optional<Championship> findById(Long id) {
        return championshipRepository.findById(id);
    }

    public Championship create(Championship championship) {
        return championshipRepository.save(championship);
    }

    public Championship save(Long id, Championship championship) {
        championship.setId(id);
        return championshipRepository.save(championship);
    }

    public void delete(Long id) {
        championshipRepository.deleteById(id);
    }

    /**
     * Calculate earnings based on placement in a championship.
     * 
     * Place | Percentage
     * 1.º Place	14%
     * 2.º Place	12.9%
     * 3.º Place	11.6%
     * 4.º Place	10.2%
     * 5.º Place	9%
     * 6.º Place	8.1%
     * 7.º Place	7.1%
     * 8.º Place	6.1%
     * 9.º Place	5.1%
     * 10.º Place	4%
     * 11 and more	0%
     */
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
            case 1 -> 0.14;
            case 2 -> 0.129;
            case 3 -> 0.116;
            case 4 -> 0.102;
            case 5 -> 0.09;
            case 6 -> 0.081;
            case 7 -> 0.071;
            case 8 -> 0.061;
            case 9 -> 0.051;
            case 10 -> 0.04;
            default -> 0.0;
        };
    }
}
