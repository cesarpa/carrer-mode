package com.cesarpacode.carrer_mode.service;

import com.cesarpacode.carrer_mode.model.Track;
import com.cesarpacode.carrer_mode.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrackService {

    @Autowired
    private TrackRepository trackRepository;

    public List<Track> getAll() {
        return trackRepository.findAll();
    }

    public Optional<Track> findById(Long id) {
        return trackRepository.findById(id);
    }

    public Track create(Track track) {
        return trackRepository.save(track);
    }

    public Track save(Long id, Track track) {
        track.setId(id);
        return trackRepository.save(track);
    }

    public void delete(Long id) {
        trackRepository.deleteById(id);
    }
}
