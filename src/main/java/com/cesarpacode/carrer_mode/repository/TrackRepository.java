package com.cesarpacode.carrer_mode.repository;

import com.cesarpacode.carrer_mode.model.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
}
