package com.cesarpacode.carrer_mode.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;

    private String name; // Ej: "McLaren F1" o "Ferrari F50"

    private Long model;

    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<GarageCar> garageCars = new java.util.ArrayList<>();

    @Transient
    private Long garageId;
}