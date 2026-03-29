package com.cesarpacode.carrer_mode.service;

import com.cesarpacode.carrer_mode.model.Pilot;
import com.cesarpacode.carrer_mode.repository.PilotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PilotService {
    @Autowired
    private PilotRepository pilotRepository;

    public List<Pilot> getAll() {
        return pilotRepository.findAll();
    }

    public Optional<Pilot> getById(Long id) {
        return pilotRepository.findById(id);
    }

    public Pilot create(Pilot pilot) {
        return pilotRepository.save(pilot);
    }

    public Pilot save(Long id, Pilot pilot) {
        pilot.setId(id);
        return pilotRepository.save(pilot);
    }
    
    public void delete(Long id) {
        pilotRepository.deleteById(id);
    }
}
