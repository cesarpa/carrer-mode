package com.cesarpacode.carrer_mode.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "garage_championship")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GarageChampionship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;

    @ManyToOne
    @JoinColumn(name = "championship_id", nullable = false)
    private Championship championship;
}
