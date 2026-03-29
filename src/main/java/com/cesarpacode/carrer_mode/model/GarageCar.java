package com.cesarpacode.carrer_mode.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "garage_car")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GarageCar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "garage_id", nullable = false)
    private Garage garage;
}
